package client.repository;

import client.entity.Client;

import java.util.Map;
import java.util.Optional;

public interface ClientRepository {
    public Client save(Client client);

    public void remove(Client client);

    public Optional<Client> findById(Long clientId);

    public Optional<Client> findByEmail(String email);

    public Map<Long, Client> findAll();

    public Long getNextId();
}
