package service;

import entity.client.Client;

public interface ClientService {
    void addClient(Client client);

    void deleteClient(Long clientId);
}
