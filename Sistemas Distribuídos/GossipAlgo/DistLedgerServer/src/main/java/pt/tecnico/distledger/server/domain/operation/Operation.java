package pt.tecnico.distledger.server.domain.operation;

import pt.tecnico.distledger.utils.VectorClock;

public class Operation {
    private String account;

    VectorClock prev;
    VectorClock timestamps;

    
    private boolean stable = false;
    
    public Operation(String fromAccount) {
        this.account = fromAccount;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    
    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }
    
    public VectorClock getPrev() {
        return this.prev;
    }
    
    public void setPrev(VectorClock prev) {
        this.prev = prev;
    }
    
    public VectorClock getTimestamps() {
        return this.timestamps;
    }

    public void setTimestamps(VectorClock timestamps) {
        this.timestamps = timestamps;
    }
}
