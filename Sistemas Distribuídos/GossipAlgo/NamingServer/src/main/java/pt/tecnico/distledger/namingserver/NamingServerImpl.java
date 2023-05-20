package pt.tecnico.distledger.namingserver;


import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.namingserver.domain.ServerEntry;
import pt.tecnico.distledger.namingserver.domain.ServiceEntry;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.grpc.Status.NOT_FOUND;

public class NamingServerImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase {

    private Map<String, ServiceEntry> serviceEntries = new HashMap<>();

    private int idCount = 0;

    public NamingServerImpl(Map<String, ServiceEntry> serviceEntries) {this.serviceEntries = serviceEntries;}



    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        if (serviceEntries.get(request.getService()) == null) {
            serviceEntries.put(request.getService(),
                    new ServiceEntry(request.getService(), request.getQualifier(), request.getAddress()));
        }
        else {
            serviceEntries.get(request.getService()).addServer(new ServerEntry(request.getAddress(), request.getQualifier()));
        }

        RegisterResponse response = RegisterResponse.newBuilder().setId(idCount++).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        try {
            serviceEntries.get(request.getService()).removeServer(request.getAddress());
            DeleteResponse response = DeleteResponse.newBuilder().build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (Exception e) {
            responseObserver.onError(NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
        
        List<DistLedgerCommonDefinitions.ServerEntry> serverEntries = new ArrayList<>();
        ServiceEntry serviceEntry = serviceEntries.get(request.getService());
        LookupResponse response = LookupResponse.getDefaultInstance();

            for (ServerEntry entry : serviceEntry.getServers()){
                if(entry.getQualifier().equals(request.getQualifier()) || request.getQualifier() == null){
                    serverEntries.add(DistLedgerCommonDefinitions.ServerEntry.newBuilder()
                    .setAddress(entry.getAddress()).setQualifier(entry.getQualifier()).build());
            }
            response = LookupResponse.newBuilder()
            .addAllEntries(serverEntries).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}