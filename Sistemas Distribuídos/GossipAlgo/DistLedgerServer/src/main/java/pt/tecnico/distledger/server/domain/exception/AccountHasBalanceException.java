package pt.tecnico.distledger.server.domain.exception;

public class AccountHasBalanceException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccountHasBalanceException(String message) {
        super(message);
    }

}
