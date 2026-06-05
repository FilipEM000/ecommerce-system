package client.repository.impl;

import client.entity.Client;
import client.repository.ClientRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryClientRepository implements ClientRepository {
    private Map<Long, Client> clients = new ConcurrentHashMap<>();
    private AtomicLong clientCounter = new AtomicLong(0);

    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(getNextId());
        }
        clients.put(client.getId(), client);
        return client;
    }

    public void remove(Client client) {
        clients.remove(client.getId());
    }

    public Optional<Client> findById(Long clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }

    public Optional<Client> findByEmail(String email) {
        return clients.values().stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    public Map<Long, Client> findAll() {
        return Collections.unmodifiableMap(clients);
    }

    public Long getNextId() {
        return clientCounter.getAndIncrement();
    }
}
