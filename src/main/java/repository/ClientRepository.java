package repository;

import entity.client.Client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientRepository {
    private Map<Long, Client> clients = new HashMap<>();
    private Long clientCounter = 0L;

    public void save(Client client){
        client.setId(getNextId());
        clients.putIfAbsent(client.getId(), client);
    }

    public void remove(Client client){
        clients.remove(client.getId());
    }

    public Optional<Client> findById(Long clientId){
        return Optional.ofNullable(clients.get(clientId));
    }

    public Map<Long, Client> findAll(){
        return Collections.unmodifiableMap(clients);
    }

    public Long getNextId(){
        return clientCounter++;
    }
}
