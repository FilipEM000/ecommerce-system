package client.service;

import client.dto.ClientDto;

public interface ClientService {
    void deleteClient(Long clientId);

    Long login(String email);

    ClientDto register(String email, String name);
}
