package pt.tecnico.distledger.server.domain.exception;

public class SameAccountException extends Exception {

    private static final long serialVersionUID = 1L;

    public SameAccountException(String message) {
        super(message);
    }

}
