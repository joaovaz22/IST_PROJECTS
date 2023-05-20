package pt.tecnico.distledger.server;

import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.domain.exception.*;
import pt.tecnico.distledger.server.grpc.ServerService;
import pt.tecnico.distledger.utils.VectorClock;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc.UserServiceImplBase;

import static io.grpc.Status.*;

public class UserServiceImpl extends UserServiceImplBase {

    private final ServerState serverState;
    private final ServerService serverService;

    public UserServiceImpl(ServerState serverState, ServerService serverService) {
        this.serverState = serverState;
        this.serverService = serverService;
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        VectorClock prev = new VectorClock(request.getPrev().getTimestampList());
        if (!serverService.getValueTimestamps().greaterOrEqual(prev)) {
            ServerMain.debug("UserServiceImpl: Outdated server error, notifying client");
            responseObserver.onError(INTERNAL.withDescription("Outdated Server\n").asRuntimeException());
            return;
        }

        try {
            // StreamObserver is used to represent the gRPC stream between the server and
            // client in order to send the appropriate responses (or errors, if any occur).

            ServerMain.debug("UserServiceImpl: Calling serverState.getBalance");
            int balance = serverState.getBalance(request.getUserId());
            BalanceResponse response = BalanceResponse.newBuilder().
                    setValue(balance).
                    setNew(serverService.getValueTimestamps().toProto()).
                    build();

            ServerMain.debug("UserServiceImpl: Sending BalanceResponse through the stream");
            // Send a single response through the stream.
            responseObserver.onNext(response);
            ServerMain.debug("UserServiceImpl: Notifying client that operation has been completed");
            // Notify the client that the operation has been completed.
            responseObserver.onCompleted();
        }
        catch (AccountNotFoundException e) {
            ServerMain.debug("UserServiceImpl: Notifying client of error");
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
        catch (ServerInactiveException e) {
            ServerMain.debug("UserServiceImpl: Notifying client of error");
            responseObserver.onError(UNAVAILABLE.asRuntimeException());
        }
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<CreateAccountResponse> responseObserver) {

        ServerMain.debug("UserServiceImpl: Calling serverState.createAccount");

        /* DistLedgerCommonDefinitions.Operation operation =  DistLedgerCommonDefinitions.Operation.newBuilder().setType(DistLedgerCommonDefinitions.OperationType.OP_CREATE_ACCOUNT).setUserId(request.getUserId()).build();
        PropagateStateResponse propagateStateResponse = this.serverService.propagateState(serverState, operation);
        // If an error occurred when propagating state
        if (!propagateStateResponse.getException().equals("")) {
            responseObserver.onError(ABORTED.withDescription(propagateStateResponse.getException()).asRuntimeException());
            return;
        } */

        int serverId = serverService.getId();
        int value = serverService.getReplicaTimestamps().getTimestamp(serverId);
        serverService.getReplicaTimestamps().setTimestamp(serverId, ++value);

        try {

            // StreamObserver is used to represent the gRPC stream between the server and
            // client in order to send the appropriate responses (or errors, if any occur).
            // serverState.createAccount(request.getUserId());
            VectorClock ts = new VectorClock(serverService.getReplicaTimestamps().getTimestamps());
            //ts.setTimestamp(serverId, value);
            serverService.createAccount(request.getUserId(), new VectorClock(request.getPrev().getTimestampList()), ts);
            CreateAccountResponse response = CreateAccountResponse.newBuilder().setNew(ts.toProto()).build();

            ServerMain.debug("UserServiceImpl: Sending CreateAccountResponse through the stream");
            // Send a single response through the stream.
            responseObserver.onNext(response);
            // Notify the client that the operation has been completed.
            ServerMain.debug("UserServiceImpl: Notifying client that operation has been completed");
            responseObserver.onCompleted();
        }
        catch (AccountAlreadyExistsException e) {
            ServerMain.debug("UserServiceImpl: Notifying client of error");
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
        catch (ServerInactiveException e) {
            ServerMain.debug("UserServiceImpl: Notifying client of error");
            responseObserver.onError(UNAVAILABLE.asRuntimeException());
        }
    }

    /* @Override
    public void deleteAccount(DeleteAccountRequest request, StreamObserver<DeleteAccountResponse> responseObserver) {
        ServerMain.debug("UserServiceImpl: Calling serverState.deleteAccount");
        DistLedgerCommonDefinitions.Operation operation =  DistLedgerCommonDefinitions.Operation.newBuilder().setType(DistLedgerCommonDefinitions.OperationType.OP_DELETE_ACCOUNT).setUserId(request.getUserId()).build();
        PropagateStateResponse propagateStateResponse = this.serverService.propagateState(operation);
        // If an error occurred when propagating state
        if (!propagateStateResponse.getException().equals("")) {
            responseObserver.onError(ABORTED.withDescription(propagateStateResponse.getException()).asRuntimeException());
            return;
        }

        try {
           // StreamObserver is used to represent the gRPC stream between the server and
           // client in order to send the appropriate responses (or errors, if any occur).

           ServerMain.debug("UserServiceImpl: Calling serverState.deleteAccount");
           serverState.deleteAccount(request.getUserId());
           DeleteAccountResponse response = DeleteAccountResponse.newBuilder().build();

           // Send a single response through the stream.
           ServerMain.debug("UserServiceImpl: Sending DeleteAccountResponse through the stream");
           responseObserver.onNext(response);
           // Notify the client that the operation has been completed.
           ServerMain.debug("UserServiceImpl: Notifying client that operation has been completed");
           responseObserver.onCompleted();
       }
       catch (AccountNotFoundException | AccountHasBalanceException | ServerInactiveException e) {
           ServerMain.debug("UserServiceImpl: Notifying client of error");
           responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
       }
    } */

    @Override
    public void transferTo(TransferToRequest request, StreamObserver<TransferToResponse> responseObserver) {

        ServerMain.debug("UserServiceImpl: Calling serverState.transferTo");
        /* DistLedgerCommonDefinitions.Operation operation =  DistLedgerCommonDefinitions.Operation.newBuilder()
                .setType(DistLedgerCommonDefinitions.OperationType.OP_TRANSFER_TO)
                .setUserId(request.getAccountFrom())
                .setDestUserId(request.getAccountTo())
                .setAmount(request.getAmount()).build();

        PropagateStateResponse propagateStateResponse = this.serverService.propagateState(operation);
        // If an error occurred when propagating state
        if (!propagateStateResponse.getException().equals("")) {
            responseObserver.onError(ABORTED.withDescription(propagateStateResponse.getException()).asRuntimeException());
            return;
        } */

        int serverId = serverService.getId();
        int value = serverService.getReplicaTimestamps().getTimestamp(serverId);
        serverService.getReplicaTimestamps().setTimestamp(serverId, ++value);

        try {
            // StreamObserver is used to represent the gRPC stream between the server and
            // client in order to send the appropriate responses (or errors, if any occur).

            ServerMain.debug("UserServiceImpl: Calling serverState.transferTo");
            VectorClock ts = new VectorClock(serverService.getReplicaTimestamps().getTimestamps());
            //ts.setTimestamp(serverId, value);
            serverService.transferTo(request.getAccountFrom(), request.getAccountTo(), request.getAmount(), new VectorClock(request.getPrev().getTimestampList()), ts);
            TransferToResponse response = TransferToResponse.newBuilder().setNew(ts.toProto()).build();

            // Send a single response through the stream.
            ServerMain.debug("UserServiceImpl: Sending TransferToResponse through the stream");
            responseObserver.onNext(response);
            // Notify the client that the operation has been completed.
            ServerMain.debug("UserServiceImpl: Notifying client that operation has been completed");
            responseObserver.onCompleted();
        }
        catch (AccountNotFoundException | SameAccountException | InvalidAmountException | InsufficientBalanceException | ServerInactiveException e) {
            ServerMain.debug("UserServiceImpl: Notifying client of error");
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
