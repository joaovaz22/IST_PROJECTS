package pt.tecnico.distledger.server;

import java.io.IOException;

import io.grpc.*;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.grpc.ServerService;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.*;


public class ServerMain {

	/** Set flag to true to print debug messages.
	 * The flag can be set using the -Ddebug command line option. */
	private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);

	/** Helper method to print debug messages. */
	public static void debug(String debugMessage) {
		if (DEBUG_FLAG)
			System.err.println(debugMessage);
	}


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(ServerMain.class.getSimpleName());

		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// check arguments
		if (args.length != 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: mvn exec:java -Dexec.args=<port> <qualifier>");
			return;
		}

		ServerState serverState = new ServerState();

		final int port = Integer.parseInt(args[0]);
		final String qualifier = args[1];
		final String service = "DistLedger";
		String target = "localhost:5001";
		ServerService serverService = new ServerService(target, serverState);


		final BindableService userServiceImpl = new UserServiceImpl(serverState, serverService);
		final BindableService adminServiceImpl = new AdminServiceImpl(serverService);
		final BindableService distLedgerCrossServerServiceImpl = new DistLedgerCrossServerServiceImpl(serverState, serverService);

		// Create a new server to listen on port
		Server server = ServerBuilder.forPort(port)
				.addService(userServiceImpl)
				.addService(adminServiceImpl)
				.addService(distLedgerCrossServerServiceImpl)
				.build();

		RegisterRequest request = RegisterRequest.newBuilder().setQualifier(qualifier).setAddress("localhost:" + port).setService(service).build();
		RegisterResponse response = serverService.register(request);
		serverService.setId(response.getId());


		// Start the server
		server.start();

		// Server threads are running in the background.
		System.out.println("Server started");

		// Do not exit the main thread. Wait until input.
		System.out.println("Press enter to shutdown");
		System.in.read();

		DeleteRequest deleteRequest = DeleteRequest.newBuilder().setAddress("localhost:" + port).setService(service).build();
		DeleteResponse deleteResponse = serverService.delete(deleteRequest);
		serverService.terminate();
		server.shutdown();
    }

}

