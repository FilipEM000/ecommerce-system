package client.repository;

import client.entity.Client;

import java.util.Map;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);

    void remove(Client client);

    Optional<Client> findById(Long clientId);

    Optional<Client> findByEmail(String email);

    Map<Long, Client> findAll();

    Long getNextId();
}
