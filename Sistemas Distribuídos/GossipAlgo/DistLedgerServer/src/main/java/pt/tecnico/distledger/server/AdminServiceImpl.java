package pt.tecnico.distledger.server;

import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.server.domain.exception.ServerAlreadyActiveException;
import pt.tecnico.distledger.server.domain.exception.ServerAlreadyInactiveException;
import pt.tecnico.distledger.server.domain.operation.CreateOp;
import pt.tecnico.distledger.server.domain.operation.DeleteOp;
import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.tecnico.distledger.server.domain.operation.TransferOp;
import pt.tecnico.distledger.server.grpc.ServerService;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;

import static io.grpc.Status.FAILED_PRECONDITION;
import static pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions.*;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminServiceGrpc.AdminServiceImplBase;

import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl extends AdminServiceImplBase {

    private final ServerService serverService;

    public AdminServiceImpl(ServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public void activate(ActivateRequest request, StreamObserver<ActivateResponse> responseObserver) {
        // StreamObserver is used to represent the gRPC stream between the server and
        // client in order to send the appropriate responses (or errors, if any occur).
        try {
            ServerMain.debug("AdminServiceImpl: Calling serverState.activate");
            this.serverService.activate();
            ActivateResponse response = ActivateResponse.newBuilder().build();

            // Send a single response through the stream.
            ServerMain.debug("AdminServiceImpl: Sending ActivateResponse through the stream");
            responseObserver.onNext(response);
            // Notify the client that the operation has been completed.
            ServerMain.debug("AdminServiceImpl: Notifying client that operation has been completed");
            responseObserver.onCompleted();
        }
        catch (ServerAlreadyActiveException e) {
            ServerMain.debug("AdminServiceImpl: Notifying client of error");
            responseObserver.onError(FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deactivate(DeactivateRequest request, StreamObserver<DeactivateResponse> responseObserver) {
        // StreamObserver is used to represent the gRPC stream between the server and
        // client in order to send the appropriate responses (or errors, if any occur).
        try {
            ServerMain.debug("AdminServiceImpl: Calling serverState.deactivate");
            this.serverService.deactivate();
            DeactivateResponse response = DeactivateResponse.newBuilder().build();

            // Send a single response through the stream.
            ServerMain.debug("AdminServiceImpl: Sending DeactivateResponse through the stream");
            responseObserver.onNext(response);
            // Notify the client that the operation has been completed.
            ServerMain.debug("AdminServiceImpl: Notifying client that operation has been completed");
            responseObserver.onCompleted();
        }
        catch (ServerAlreadyInactiveException e) {
            ServerMain.debug("AdminServiceImpl: Notifying client of error");
            responseObserver.onError(FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void gossip(GossipRequest request, StreamObserver<GossipResponse> responseObserver) {
        // StreamObserver is used to represent the gRPC stream between the server and
        // client in order to send the appropriate responses (or errors, if any occur).

        ServerMain.debug("AdminServiceImpl: Gossip called");
        GossipResponse response = GossipResponse.newBuilder().build();

        ServerMain.debug("AdminServiceImpl: Calling serverService.propagateState");
        this.serverService.propagateState(request.getServerTo());
        // Send a single response through the stream.
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.

        responseObserver.onCompleted();
    }

    @Override
    public void getLedgerState(getLedgerStateRequest request, StreamObserver<getLedgerStateResponse> responseObserver) {
        // StreamObserver is used to represent the gRPC stream between the server and
        // client in order to send the appropriate responses (or errors, if any occur).
        List<DistLedgerCommonDefinitions.Operation> ops = new ArrayList<>();

        ServerMain.debug("AdminServiceImpl: Building ledger using serverState.getLedgerState");
        for (Operation op: this.serverService.getLedgerState()) {
            OperationType type;
            if (op instanceof CreateOp) {
                type = OperationType.OP_CREATE_ACCOUNT;
            }else if (op instanceof DeleteOp) {
                type = OperationType.OP_DELETE_ACCOUNT;
            }else if (op instanceof TransferOp) {
                type = OperationType.OP_TRANSFER_TO;
            } else {
                type = OperationType.OP_UNSPECIFIED;
            }

            if (op instanceof TransferOp) {
                ops.add(DistLedgerCommonDefinitions.Operation.newBuilder()
                        .setType(type)
                        .setUserId(op.getAccount())
                        .setDestUserId(((TransferOp) op).getDestAccount())
                        .setAmount(((TransferOp) op).getAmount())
                        .setPrev(op.getPrev().toProto())
                        .setTimestamps(op.getTimestamps().toProto())
                        .setStable(op.isStable())
                        .build());
            } else {
                ops.add(DistLedgerCommonDefinitions.Operation.newBuilder()
                        .setType(type)
                        .setUserId(op.getAccount())
                        .setPrev(op.getPrev().toProto())
                        .setTimestamps(op.getTimestamps().toProto())
                        .setStable(op.isStable())
                        .build());
            }

        }
        LedgerState ledgerState = LedgerState.newBuilder().addAllLedger(ops).build();

        getLedgerStateResponse response = getLedgerStateResponse.newBuilder()
                .setLedgerState(ledgerState).build();

        // Send a single response through the stream.
        ServerMain.debug("AdminServiceImpl: Sending getLedgerStateResponse through the stream");
        responseObserver.onNext(response);
        // Notify the client that the operation has been completed.
        ServerMain.debug("AdminServiceImpl: Notifying client that operation has been completed");
        responseObserver.onCompleted();
    }
}
