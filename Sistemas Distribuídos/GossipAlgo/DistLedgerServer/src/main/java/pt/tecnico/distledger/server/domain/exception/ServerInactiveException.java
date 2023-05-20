package pt.tecnico.distledger.server.domain.exception;

public class ServerInactiveException extends Exception{
    private static final long serialVersionUID = 1L;

    public ServerInactiveException(String message) {
        super(message);
    }

}
