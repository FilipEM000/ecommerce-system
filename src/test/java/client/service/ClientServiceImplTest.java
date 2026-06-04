package client.service;

import client.dto.ClientDto;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.impl.ClientServiceImpl;
import exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientServiceImpl clientServiceImpl;

    @Test
    void shouldDeleteClientThrowClientNotFoundException() {
        when(clientRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientServiceImpl.deleteClient(1L))
                .extracting(ClientNotFoundException::getMessage).isEqualTo("Nie znaleziono klienta o id 1");
    }

    @Test
    void shouldRegisterNewClient() {
        Client mockedClient = new Client("Filip", "test@wp.pl");
        mockedClient.setId(1L);
        when(clientRepository.save(any()))
                .thenReturn(mockedClient);

        ClientDto result = clientServiceImpl.register("filip@wp.pl", "Filip");
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        verify(clientRepository).save(clientCaptor.capture());
        assertThat(clientCaptor.getValue().getEmail()).isEqualTo("filip@wp.pl");
        assertThat(clientCaptor.getValue().getName()).isEqualTo("Filip");
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldLoginSuccessfully() {
        Client client = new Client("Filip", "test@wp.pl");
        client.setId(1L);
        when(clientRepository.findByEmail(any()))
                .thenReturn(Optional.of(client));

        assertThat(clientServiceImpl.login("test@wp.pl")).isEqualTo(1L);
    }

    @Test
    void shouldLoginThrowClientNotFoundException() {
        when(clientRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientServiceImpl.login("test@wp.pl"))
                .extracting(ClientNotFoundException::getMessage)
                .isEqualTo("Nie znaleziono klienta z mailem test@wp.pl");
    }
}
