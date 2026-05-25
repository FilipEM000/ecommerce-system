package service.impl;

import entity.Client;
import exception.ClientNotFoundException;
import lombok.AllArgsConstructor;
import repository.ClientRepository;
import service.ClientManager;

@AllArgsConstructor
public class ClientManagerImpl implements ClientManager {
    private final ClientRepository clientRepository;

    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void deleteClient(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znalezioni klienta o id " + clientId));

        clientRepository.remove(client);
    }
}
