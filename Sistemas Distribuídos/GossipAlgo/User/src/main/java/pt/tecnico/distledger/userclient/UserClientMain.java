package pt.tecnico.distledger.userclient;


import pt.tecnico.distledger.userclient.grpc.UserService;

public class UserClientMain {

    /** Set flag to true to print debug messages.
     * The flag can be set using the -Ddebug command line option. */
    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);

    /** Helper method to print debug messages. */
    public static void debug(String debugMessage) {
        if (DEBUG_FLAG)
            System.err.println(debugMessage);
    }

    public static void main(String[] args) {

        System.out.println(UserClientMain.class.getSimpleName());

        /* // receive and print arguments
        System.out.printf("Received %d arguments%n", args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.printf("arg[%d] = %s%n", i, args[i]);
        }

        // check arguments
        if (args.length != 2) {
            System.err.println("Argument(s) missing!");
            System.err.println("Usage: mvn exec:java -Dexec.args=<host> <port>");
            return;
        } */

        final String host = "localhost";
        final int port = 5001;
        UserService service = new UserService(host, port);
        CommandParser parser = new CommandParser(service);
        parser.parseInput();

        service.terminate();
    }
}
