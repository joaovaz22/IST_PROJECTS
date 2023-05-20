package pt.tecnico.distledger.server.domain.exception;

public class ServerAlreadyActiveException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServerAlreadyActiveException(String message) {
        super(message);
    }

}
