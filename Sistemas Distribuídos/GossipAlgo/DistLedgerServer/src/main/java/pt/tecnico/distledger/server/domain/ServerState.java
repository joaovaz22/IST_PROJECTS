package pt.tecnico.distledger.server.domain;

import pt.tecnico.distledger.server.domain.exception.*;
import pt.tecnico.distledger.server.domain.operation.CreateOp;
import pt.tecnico.distledger.server.domain.operation.DeleteOp;
import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.tecnico.distledger.server.domain.operation.TransferOp;

import java.util.*;

public class ServerState {
    private List<Operation> ledger;

    private Map<String, Integer> accounts;

    private boolean isActive;

    public ServerState() {
        this.ledger = new ArrayList<>();
        this.accounts = new HashMap<>();
        this.isActive = true;
        accounts.put("broker", 1000);
    }

    public void activate() throws ServerAlreadyActiveException {
        if (!this.isActive) {
            this.isActive = true;
        }
        else {
            throw new ServerAlreadyActiveException("Server is already active\n");
        }
    }

    public void deactivate() throws ServerAlreadyInactiveException {
        if (this.isActive) {
            this.isActive = false;
        }
        else {
            throw new ServerAlreadyInactiveException("Server is already inactive\n");
        }
    }

    public List<Operation> getLedgerState() {
        return this.ledger;
    }

    public synchronized Integer getBalance(String account) throws AccountNotFoundException, ServerInactiveException {

        if (!this.isActive) {
            throw new ServerInactiveException("\n");
        }

        Integer balance = accounts.get(account);
        if (balance == null) {
            throw new AccountNotFoundException("Account " + account + " does not exist\n");
        }
        return balance;
    }

    public synchronized Operation createAccount(String account) throws AccountAlreadyExistsException, ServerInactiveException {

        if (!this.isActive) {
            throw new ServerInactiveException("\n");
        }

        /* if (accounts.get(account) != null) {
            throw new AccountAlreadyExistsException("Account " + account + " already exists\n");
        } */
        Operation op = new CreateOp(account);
        ledger.add(op);
        return op;
    }

    public synchronized void execute(Operation op) {
        op.setStable(true);
        if (op instanceof CreateOp) {
            if (accounts.get(op.getAccount()) != null) { return; }
            accounts.put(op.getAccount(), 0);
        }
        else { // Transfer
            TransferOp transferOp = (TransferOp)op;
            String fromAccount = transferOp.getAccount();
            String destAccount = transferOp.getDestAccount();
            Integer amount = transferOp.getAmount();
            if (accounts.get(fromAccount) == null) { return; }
            if (accounts.get(destAccount) == null) { return; }
            if (amount <= 0) { return; }
            if (Objects.equals(fromAccount, destAccount)) { return; }
            if (accounts.get(fromAccount) < amount) { return; }
            accounts.replace(fromAccount, accounts.get(fromAccount) - amount);
            accounts.replace(destAccount, accounts.get(destAccount) + amount);
        }
    }

    public synchronized void deleteAccount(String account) throws AccountNotFoundException, ServerInactiveException, AccountHasBalanceException {

        if (!this.isActive) {
            throw new ServerInactiveException("\n");
        }

        Integer balance = accounts.get(account);
        if (balance == null) {
            throw new AccountNotFoundException("Account " + account + " does not exist\n");
        }
        if (balance > 0) {
            throw new AccountHasBalanceException("Account " + account + "does not have 0 balance. Current amount: " + balance + "\n");
        }
        Operation op = new DeleteOp(account);
        ledger.add(op);
    }

    public synchronized Operation transferTo(String fromAccount, String destAccount, Integer amount) throws AccountNotFoundException, SameAccountException, InsufficientBalanceException, ServerInactiveException, InvalidAmountException {

        // Check all fail conditions
        if (!this.isActive) {
            throw new ServerInactiveException("\n");
        }
        /* if (accounts.get(fromAccount) == null) {
            throw new AccountNotFoundException("Account " + fromAccount + " does not exist\n");
        }
        if (accounts.get(destAccount) == null) {
            throw new AccountNotFoundException("Account " + destAccount + " does not exist\n");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException("Invalid amount. Amount has to be a positive number\n");
        }
        if (Objects.equals(fromAccount, destAccount)) {
            throw new SameAccountException("Cannot transfer to the same account\n");
        }
        if (accounts.get(fromAccount) < amount) {
            throw new InsufficientBalanceException("Account " + fromAccount + " does not have enough balance for this transfer\n");
        } */

        Operation op = new TransferOp(fromAccount, destAccount, amount);
        ledger.add(op);
        return op;
    }

    public synchronized void clean() throws ServerInactiveException {

        if (!isActive) {
            throw new ServerInactiveException("Secondary server Inactive");
        }

        this.ledger = new ArrayList<>();
        this.accounts = new HashMap<>();
        accounts.put("broker", 1000);
    }
}
