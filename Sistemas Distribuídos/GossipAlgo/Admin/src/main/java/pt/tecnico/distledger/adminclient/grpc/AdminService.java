package pt.tecnico.distledger.adminclient.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.distledger.adminclient.AdminClientMain;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;

import java.util.HashMap;
import java.util.Map;


public class AdminService {

    /* TODO: The gRPC client-side logic should be here.
        This should include a method that builds a channel and stub,
        as well as individual methods for each remote operation of this service.*/

    private final ManagedChannel namingChannel;
    private final NamingServerServiceGrpc.NamingServerServiceBlockingStub namingStub;

    private Map<String, ManagedChannel> channels = new HashMap<>();
    private Map<String, AdminServiceGrpc.AdminServiceBlockingStub> stubs = new HashMap<>();

    public AdminService(String host, int port) {
        String target = host + ":" + port;
        AdminClientMain.debug("AdminService: Creating namingChannel with plaintext communication");
        this.namingChannel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        AdminClientMain.debug("AdminService: Creating blocking stub for naming server");
        this.namingStub = NamingServerServiceGrpc.newBlockingStub(namingChannel);
    }

    public void lookup(String service, String qualifier) {
        NamingServerDistLedger.LookupRequest request = NamingServerDistLedger.LookupRequest.newBuilder().setService(service).setQualifier(qualifier).build();
        AdminClientMain.debug("AdminService: Looking up server in NamingServer");
        NamingServerDistLedger.LookupResponse response = this.namingStub.lookup(request);
        for (DistLedgerCommonDefinitions.ServerEntry entry : response.getEntriesList()) {
            this.channels.put(qualifier, ManagedChannelBuilder.forTarget(entry.getAddress()).usePlaintext().build());
            this.stubs.put(qualifier, AdminServiceGrpc.newBlockingStub(this.channels.get(qualifier)));
        }
    }

    public void terminate() {
        AdminClientMain.debug("AdminService: Shutting down channels");
        this.namingChannel.shutdownNow();
        channels.forEach((q, channel) -> channel.shutdownNow());

    }

    public void activate(String server) {
        ActivateRequest request = ActivateRequest.newBuilder().build();
        ActivateResponse response;

        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            System.out.println("Invalid server + " + server + ".");
            return;
        }

        AdminClientMain.debug("AdminService: Calling activate through stub " + server);
        response = this.stubs.get(server).activate(request);


        System.out.println("OK\n");
    }

    public void deactivate(String server) {
        DeactivateRequest request = DeactivateRequest.newBuilder().build();
        DeactivateResponse response;
        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            System.out.println("Invalid server + " + server + ".");
            return;
        }
        AdminClientMain.debug("AdminService: Calling deactivate through stub " + server);
        response = this.stubs.get(server).deactivate(request);




        System.out.println("OK\n");
    }

    public void gossip(String serverFrom, String serverTo) {
        GossipRequest request = GossipRequest.newBuilder().setServerTo(serverTo).build();
        GossipResponse response;

        if (this.stubs.get(serverFrom) == null) {
            lookup("DistLedger", serverFrom);
        }
        if (this.stubs.get(serverFrom) == null) {
            System.out.println("Invalid server + " + serverFrom + ".");
            return;
        }
        AdminClientMain.debug("AdminService: Calling gossip through stub " + serverFrom);
        response = this.stubs.get(serverFrom).gossip(request);


        System.out.println("OK");
        System.out.println(response.toString());
    }

    public void getLedgerState(String server) {


        getLedgerStateRequest request = getLedgerStateRequest.newBuilder().build();
        getLedgerStateResponse response;


        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            System.out.println("Invalid server + " + server + ".");
            return;
        }

        AdminClientMain.debug("AdminService: Calling getLedgerState through stub " + server);
        response = this.stubs.get(server).getLedgerState(request);


        System.out.println("OK");
        System.out.println(response.toString());
        //Print State
    }
}
