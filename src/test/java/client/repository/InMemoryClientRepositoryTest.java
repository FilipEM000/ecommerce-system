package client.repository;

import client.entity.Client;
import client.repository.impl.InMemoryClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryClientRepositoryTest {

    private InMemoryClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository = new InMemoryClientRepository();
    }

    @Test
    void shouldSaveClientAndAssignId() {
        Client client = new Client("Filip", "filip@wp.pl");

        Client savedClient = clientRepository.save(client);

        assertThat(savedClient.getId()).isNotNull();
        assertThat(clientRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldFindClientByEmail() {
        Client client = new Client("Filip", "filip@wp.pl");
        clientRepository.save(client);

        Client foundClient = clientRepository.findByEmail("filip@wp.pl").get();

        assertThat(foundClient.getName()).isEqualTo("Filip");
    }

    @Test
    void shouldUpdateClientWithoutChangingId() {
        Client client = new Client("Filip", "filip@wp.pl");
        clientRepository.save(client);

        client.getCart().getProducts().put(null, 1);
        Client updatedClient = clientRepository.save(client);

        assertThat(updatedClient.getId()).isEqualTo(0L);
        assertThat(clientRepository.findAll()).hasSize(1);
    }
}
