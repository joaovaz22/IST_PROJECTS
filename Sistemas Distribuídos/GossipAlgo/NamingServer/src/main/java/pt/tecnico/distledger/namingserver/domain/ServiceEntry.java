package pt.tecnico.distledger.namingserver.domain;

import io.grpc.Server;

import java.util.ArrayList;
import java.util.List;

public class ServiceEntry {

    private String service;
    private List<ServerEntry> serverEntries = new ArrayList<>();

    public ServiceEntry(String service, String qualifier, String address) {
        this.setService(service);
        serverEntries.add(new ServerEntry(address, qualifier));
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public List<ServerEntry> getServers() {
        return serverEntries;
    }

    public void addServer(ServerEntry server) {
        this.serverEntries.add(server);
    }

    public void removeServer(String address) throws Exception {
        for (ServerEntry entry : serverEntries) {
            if (entry.getAddress().equals(address)) {
                serverEntries.remove(entry);
                return;
            }
        }
        throw new Exception("Not possible to remove the server");

    }
}
