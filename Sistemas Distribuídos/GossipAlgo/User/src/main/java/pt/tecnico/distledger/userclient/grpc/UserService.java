package pt.tecnico.distledger.userclient.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.distledger.userclient.UserClientMain;
import pt.tecnico.distledger.utils.VectorClock;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.*;
import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    /*TODO: The gRPC client-side logic should be here.
        This should include a method that builds a channel and stub,
        as well as individual methods for each remote operation of this service. */

    private final ManagedChannel namingChannel;
    private final NamingServerServiceGrpc.NamingServerServiceBlockingStub namingStub;

    private Map<String, ManagedChannel> channels = new HashMap<>();

    private Map<String,UserServiceGrpc.UserServiceBlockingStub> stubs = new HashMap<>();

    private VectorClock timestamps;

    public UserService(String host, int port) {
        String target = host + ":" + port;
        UserClientMain.debug("UserService: Creating namingChannel with plaintext communication");
        this.namingChannel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        UserClientMain.debug("UserService: Creating blocking stub for naming server");
        this.namingStub = NamingServerServiceGrpc.newBlockingStub(namingChannel);
        this.timestamps = new VectorClock(0);
    }

    public void lookup(String service, String qualifier) {
        LookupRequest request = LookupRequest.newBuilder().setService(service).setQualifier(qualifier).build();
        UserClientMain.debug("UserService: Looking up server in NamingServer");
        LookupResponse response = this.namingStub.lookup(request);
        for (DistLedgerCommonDefinitions.ServerEntry entry : response.getEntriesList()) {
            UserClientMain.debug("UserService: Creating channel " + qualifier + " with plaintext communication");
            this.channels.put(qualifier, ManagedChannelBuilder.forTarget(entry.getAddress()).usePlaintext().build());
            UserClientMain.debug("UserService: Creating blocking stub for server " + qualifier);
            this.stubs.put(qualifier, UserServiceGrpc.newBlockingStub(this.channels.get(qualifier)));
        }
    }

    public void terminate() {
        UserClientMain.debug("UserService: Shutting down channels");
        this.namingChannel.shutdownNow();
        this.channels.forEach((qualifier, channel) -> channel.shutdownNow());

    }

    public void createAccount(String username, String server) {
        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            //Add other server
            System.out.println("Invalid server " + server + ".");
            return;
        }
        UserClientMain.debug("UserService: prev " + timestamps.getTimestamps());
        CreateAccountRequest request = CreateAccountRequest.newBuilder().setUserId(username).setPrev(timestamps.toProto()).build();
        UserClientMain.debug("UserService: Calling createAccount through stub");
        CreateAccountResponse response = this.stubs.get(server).createAccount(request);
        timestamps.merge(new VectorClock(response.getNew().getTimestampList()));
        UserClientMain.debug("UserService: new " + timestamps.getTimestamps());
        System.out.println("OK\n");
    }

    /*
    public void deleteAccount(String username, String server) {
        if (server.equals("B")) {
            System.out.println("Can't call write operations through secondary server\n");
            return;
        }
        if (!server.equals("A")) {
            System.out.println("Invalid server + " + server + ". Use 'A' or 'B'");
            return;
        }

        // Check if stubA already exists, if not, lookup primary server
        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }

        DeleteAccountRequest request = DeleteAccountRequest.newBuilder().setUserId(username).build();
        UserClientMain.debug("UserService: Calling deleteAccount through stub");
        DeleteAccountResponse response = this.stubs.get(server).deleteAccount(request);
        System.out.println("OK\n");
    }
    */

    public void balance(String username, String server) {
        BalanceRequest request = BalanceRequest.newBuilder().setUserId(username).setPrev(timestamps.toProto()).build();
        BalanceResponse response;

        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            System.out.println("Invalid server " + server + ".");
            return;
        }

        UserClientMain.debug("UserService: Calling balance through stub " + server);
        response = this.stubs.get(server).balance(request);

        timestamps.merge(new VectorClock(response.getNew().getTimestampList()));
        System.out.println("OK");
        System.out.println(response.getValue() + "\n");
    }

    public void transferTo(String from, String dest, int amount, String server) {
        if (this.stubs.get(server) == null) {
            lookup("DistLedger", server);
        }
        if (this.stubs.get(server) == null) {
            System.out.println("Invalid server " + server + ".");
            return;
        }

        TransferToRequest request = TransferToRequest.newBuilder().setAccountFrom(from)
                .setAccountTo(dest).setAmount(amount).setPrev(timestamps.toProto()).build();

        UserClientMain.debug("UserService: Calling transferTo through stub");
        TransferToResponse response = this.stubs.get(server).transferTo(request);
        timestamps.merge(new VectorClock(response.getNew().getTimestampList()));
        System.out.println("OK\n");
    }



}
