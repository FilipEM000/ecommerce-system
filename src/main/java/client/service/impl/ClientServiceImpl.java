package client.service.impl;

import client.dto.ClientDto;
import client.entity.Client;
import client.mapper.ClientMapper;
import client.repository.ClientRepository;
import client.service.ClientService;
import client.validator.ClientValidator;
import exception.ClientNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;


    @Override
    public void deleteClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));

        clientRepository.remove(client);
    }

    @Override
    public Long login(String email) {
        Client client = clientRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta z mailem " + email));

        return client.getId();
    }

    @Override
    public ClientDto register(String email, String name) {
        String normalizedEmail = email.trim().toLowerCase();

        ClientValidator.validateRegistration(normalizedEmail, name);
        ClientValidator.validateEmailNotTaken(normalizedEmail, clientRepository);

        Client client = new Client(name, normalizedEmail);
        Client savedClient = clientRepository.save(client);
        return ClientMapper.mapToDto(savedClient);
    }
}
