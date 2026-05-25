package repository;

import entity.Client;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ClientRepository {
    private Map<Long, Client> clients;

    public void save(Client client){
        client.setId(getNextId());
        clients.putIfAbsent(client.getId(), client);
    }

    public void remove(Client client){
        clients.remove(client.getId());
    }

    public Optional<Client> findById(long clientId){
        return Optional.ofNullable(clients.get(clientId));
    }

    public Map<Long, Client> findAll(){
        return Collections.unmodifiableMap(clients);
    }

    public long getNextId(){
        return clients.size()+1;
    }
}
