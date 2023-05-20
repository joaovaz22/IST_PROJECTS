package pt.tecnico.distledger.server.domain.exception;

public class AccountAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccountAlreadyExistsException(String message) {
        super(message);
    }

}
