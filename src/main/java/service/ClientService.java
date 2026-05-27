package service;

import entity.Client;

public interface ClientService {
    void addClient(Client client);

    void deleteClient(Long clientId);
}
