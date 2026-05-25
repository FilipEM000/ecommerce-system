package service;

import entity.Client;

public interface ClientManager {
    void addClient(Client client);

    void deleteClient(long clientId);
}
