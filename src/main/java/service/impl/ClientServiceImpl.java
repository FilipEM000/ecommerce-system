package service.impl;

import entity.Client;
import exception.ClientNotFoundException;
import lombok.AllArgsConstructor;
import repository.ClientRepository;
import service.ClientService;

@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));

        clientRepository.remove(client);
    }
}
