package pt.tecnico.distledger.server;

import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.UNAVAILABLE;
import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.domain.exception.ServerInactiveException;
import pt.tecnico.distledger.server.grpc.ServerService;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.DistLedgerCrossServerServiceGrpc.*;

public class DistLedgerCrossServerServiceImpl extends DistLedgerCrossServerServiceImplBase {

    ServerState serverState;
    ServerService serverService;
    public DistLedgerCrossServerServiceImpl(ServerState state, ServerService service) {
        this.serverState = state;
        this.serverService = service;
    }
    @Override
    public void propagateState(PropagateStateRequest request, StreamObserver<PropagateStateResponse> responseObserver) {
        // ServerMain.debug("DistLedgerCrossServerImpl: Calling serverState.getBalance");
        PropagateStateResponse response;
        try {
            serverService.updateState(request.getState(), request.getReplicaTimestamp());
            response = PropagateStateResponse.newBuilder().setException("").build();
        }
        catch (ServerInactiveException e) {
            response = PropagateStateResponse.newBuilder().setException(UNAVAILABLE.withDescription(e.getMessage()).asRuntimeException().getMessage()).build();
        }
        catch (Exception e) {
            response = PropagateStateResponse.newBuilder().setException(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException().getMessage()).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
