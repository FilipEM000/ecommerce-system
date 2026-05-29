package service;

import dto.ClientDto;
import entity.client.Client;

public interface ClientService {
    ClientDto addClient(Client client);

    void deleteClient(Long clientId);
}
