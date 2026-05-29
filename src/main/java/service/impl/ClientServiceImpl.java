package service.impl;

import dto.ClientDto;
import entity.client.Client;
import exception.ClientNotFoundException;
import lombok.AllArgsConstructor;
import mapper.ClientMapper;
import repository.ClientRepository;
import service.ClientService;

@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public ClientDto addClient(Client client) {
        Client savedClient = clientRepository.save(client);
        return ClientMapper.mapToDto(savedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));

        clientRepository.remove(client);
    }
}
