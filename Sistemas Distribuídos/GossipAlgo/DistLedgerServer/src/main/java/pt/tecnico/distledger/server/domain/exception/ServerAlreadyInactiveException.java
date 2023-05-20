package pt.tecnico.distledger.server.domain.exception;

public class ServerAlreadyInactiveException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServerAlreadyInactiveException(String message) {
        super(message);
    }

}
