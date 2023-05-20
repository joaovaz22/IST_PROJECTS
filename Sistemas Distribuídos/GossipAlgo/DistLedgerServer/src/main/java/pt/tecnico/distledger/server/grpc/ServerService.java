package pt.tecnico.distledger.server.grpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.distledger.server.ServerMain;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.domain.exception.*;
import pt.tecnico.distledger.server.domain.operation.CreateOp;
import pt.tecnico.distledger.server.domain.operation.DeleteOp;
import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.tecnico.distledger.server.domain.operation.TransferOp;
import pt.tecnico.distledger.utils.VectorClock;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.DistLedgerCrossServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.DistLedgerCrossServerServiceGrpc.*;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc.*;

public class ServerService {

    private ManagedChannel namingChannel;
    private NamingServerServiceBlockingStub namingStub;

    private Map<String, ManagedChannel> channels = new HashMap<>();
    private Map<String, DistLedgerCrossServerServiceBlockingStub> stubs = new HashMap<>();


    private int id;

    private ServerState serverState;

    private VectorClock replicaTimestamps;
    private VectorClock valueTimestamps;

    public ServerService(String address, ServerState serverState) {
        ServerMain.debug("ServerService: Creating namingChannel with plaintext communication");
        this.namingChannel = ManagedChannelBuilder.forTarget(address).usePlaintext().build();
        ServerMain.debug("ServerService: Creating blocking stub for naming server");
        this.namingStub = NamingServerServiceGrpc.newBlockingStub(namingChannel);
        this.serverState = serverState;
        this.replicaTimestamps = new VectorClock(id);
        this.valueTimestamps = new VectorClock(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public VectorClock getReplicaTimestamps() {
        return replicaTimestamps;
    }

    public VectorClock getValueTimestamps() {
        return valueTimestamps;
    }

    public RegisterResponse register(RegisterRequest request) {
        return this.namingStub.register(request);
    }

    public void lookup(String service, String qualifier) {
        LookupRequest request = LookupRequest.newBuilder().setService(service).setQualifier(qualifier).build();
        ServerMain.debug("ServerService: Looking up server in NamingServer");
        LookupResponse response = this.namingStub.lookup(request);
        for (DistLedgerCommonDefinitions.ServerEntry entry : response.getEntriesList()) {
            ServerMain.debug("ServerService: Creating channel with plaintext communication");
            this.channels.put(entry.getQualifier(), ManagedChannelBuilder.forTarget(entry.getAddress()).usePlaintext().build());
            ServerMain.debug("ServerService: Creating blocking stub for the other server");
            this.stubs.put(entry.getQualifier(), DistLedgerCrossServerServiceGrpc.newBlockingStub(this.channels.get(entry.getQualifier())));
        }
    }

    public void propagateState(String qualifier) {
        
        List<DistLedgerCommonDefinitions.Operation> ops = new ArrayList<>();
        for (Operation op: serverState.getLedgerState()) {
            DistLedgerCommonDefinitions.OperationType type;
            if (op instanceof CreateOp) {
                type = DistLedgerCommonDefinitions.OperationType.OP_CREATE_ACCOUNT;
            }else if (op instanceof DeleteOp) {
                type = DistLedgerCommonDefinitions.OperationType.OP_DELETE_ACCOUNT;
            }else if (op instanceof TransferOp) {
                type = DistLedgerCommonDefinitions.OperationType.OP_TRANSFER_TO;
            } else {
                type = DistLedgerCommonDefinitions.OperationType.OP_UNSPECIFIED;
            }

            if (op instanceof TransferOp) {
                ops.add(DistLedgerCommonDefinitions.Operation.newBuilder()
                        .setType(type)
                        .setUserId(op.getAccount())
                        .setDestUserId(((TransferOp) op).getDestAccount())
                        .setAmount(((TransferOp) op).getAmount())
                        .setTimestamps(op.getTimestamps().toProto())
                        .setPrev(op.getPrev().toProto())
                        .build());
            } else {
                ops.add(DistLedgerCommonDefinitions.Operation.newBuilder()
                        .setType(type)
                        .setUserId(op.getAccount())
                        .setTimestamps(op.getTimestamps().toProto())
                        .setPrev(op.getPrev().toProto())
                        .build());
            }
        }
        DistLedgerCommonDefinitions.LedgerState ledgerState = DistLedgerCommonDefinitions.LedgerState.newBuilder().addAllLedger(ops).build();
        PropagateStateRequest request = PropagateStateRequest.newBuilder().setState(ledgerState).setReplicaTimestamp(this.getReplicaTimestamps().toProto()).build();

        if (this.stubs.get(qualifier) == null) {
            lookup("DistLedger", qualifier);
        }
        this.stubs.get(qualifier).propagateState(request);
    }

    public void updateState(DistLedgerCommonDefinitions.LedgerState otherServerState, DistLedgerCommonDefinitions.VectorClock ts) throws ServerInactiveException, AccountAlreadyExistsException, AccountHasBalanceException, AccountNotFoundException, SameAccountException, InsufficientBalanceException, InvalidAmountException {
        //this.serverState.clean(); //Resets the serverState
        ServerMain.debug("ServerService: replicaTimestamps before update -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps before update -> " + this.valueTimestamps.getTimestamps());

        VectorClock otherReplicaTimestamps = new VectorClock(ts.getTimestampList());

        for (DistLedgerCommonDefinitions.Operation op : otherServerState.getLedgerList()) {
            VectorClock opTimestamps = new VectorClock(op.getTimestamps().getTimestampList());
            // op.TS > this.replicaTS
            if (!this.replicaTimestamps.greaterOrEqual(opTimestamps)) {
                VectorClock opPrev = new VectorClock(op.getPrev().getTimestampList());
                if (op.getType().equals(DistLedgerCommonDefinitions.OperationType.OP_CREATE_ACCOUNT)) {
                    this.createAccount(op.getUserId(), opPrev, opTimestamps);
                } else if (op.getType().equals(DistLedgerCommonDefinitions.OperationType.OP_TRANSFER_TO)) {
                    this.transferTo(op.getUserId(), op.getDestUserId(), op.getAmount(), opPrev, opTimestamps);
                }
            }
        }

        this.replicaTimestamps.merge(otherReplicaTimestamps);

        //Loop through operations until there is no more possible executions
        boolean updated;
        do {
            updated = false;
            for (Operation op : serverState.getLedgerState()) {
                if (this.valueTimestamps.greaterOrEqual(op.getPrev()) && !op.isStable()) {
                    op.setStable(true);
                    serverState.execute(op);
                    this.valueTimestamps.merge(op.getTimestamps());
                    updated = true;
                }
            }
        } while (updated);

        ServerMain.debug("ServerService: replicaTimestamps after update -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps after update -> " + this.valueTimestamps.getTimestamps());
    }

    public DeleteResponse delete(DeleteRequest deleteRequest) {
        return this.namingStub.delete(deleteRequest);
    }

    public void createAccount(String account, VectorClock prev, VectorClock ts) throws AccountAlreadyExistsException, ServerInactiveException {
        ServerMain.debug("ServerService: replicaTimestamps before createAccount -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps before createAccount -> " + this.valueTimestamps.getTimestamps());

        Operation op = serverState.createAccount(account);
        op.setPrev(prev);
        op.setTimestamps(ts);
        if (valueTimestamps.greaterOrEqual(prev)) {
            // Stable
            serverState.execute(op);
            valueTimestamps.merge(ts);
        }
        ServerMain.debug("ServerService: replicaTimestamps after createAccount -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps after createAccount -> " + this.valueTimestamps.getTimestamps());
    }

    public void transferTo(String fromAccount, String destAccount, int amount, VectorClock prev, VectorClock ts) throws ServerInactiveException, SameAccountException, InsufficientBalanceException, InvalidAmountException, AccountNotFoundException {
        Operation op = serverState.transferTo(fromAccount, destAccount, amount);
        ServerMain.debug("ServerService: replicaTimestamps before transferTo -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps before transferTo -> " + this.valueTimestamps.getTimestamps());
        op.setPrev(prev);
        op.setTimestamps(ts);

        if (valueTimestamps.greaterOrEqual(prev)) {
            // Stable
            serverState.execute(op);
            valueTimestamps.merge(ts);
        }
        ServerMain.debug("ServerService: replicaTimestamps after transferTo -> " + this.replicaTimestamps.getTimestamps());
        ServerMain.debug("ServerService: valueTimeStamps after transferTo -> " + this.valueTimestamps.getTimestamps());
    }

    public void activate() throws ServerAlreadyActiveException {
        this.serverState.activate();
    }

    public void deactivate() throws ServerAlreadyInactiveException {
        this.serverState.deactivate();
    }

    public List<Operation> getLedgerState() {
        return this.serverState.getLedgerState();
    }

    public void terminate() {
        ServerMain.debug("ServerService: Shutting down channels");
        this.namingChannel.shutdownNow();
        this.channels.forEach((qualifier, channel) -> channel.shutdownNow());

    }
}


