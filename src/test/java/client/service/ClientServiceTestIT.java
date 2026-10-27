package client.service;

import client.dto.ClientDto;
import client.repository.impl.InMemoryClientRepository;
import client.service.impl.ClientServiceImpl;
import exception.ClientNotFoundException;
import exception.EmailAlreadyExistsException;
import exception.InvalidClientDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ClientServiceTestIT {
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientServiceImpl(new InMemoryClientRepository());
    }

    @Test
    void shouldRegisterAndLoginClient() {
        ClientDto registered = clientService.register("filip@wp.pl", "Filip");

        Long loggedClientId = clientService.login("filip@wp.pl");

        assertThat(loggedClientId).isEqualTo(registered.id());
    }

    @Test
    void shouldThrowExceptionWhenLoginWithUnknownEmail() {
        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientService.login("filip@wp.pl"))
                .extracting(ClientNotFoundException::getMessage)
                .isEqualTo("Nie znaleziono klienta z mailem filip@wp.pl");
    }

    @Test
    void shouldThrowExceptionWhenRegisteredWithInvalidEmail() {
        assertThatExceptionOfType(InvalidClientDataException.class)
                .isThrownBy(() -> clientService.register("filip", "Filip"))
                .extracting(InvalidClientDataException::getMessage)
                .isEqualTo("Nieprawidłowy adres email: filip");
    }

    @Test
    void shouldThrowExceptionWhenRegisteringWithTooShortName() {
        assertThatExceptionOfType(InvalidClientDataException.class)
                .isThrownBy(() -> clientService.register("jan@wp.pl", "J"))
                .extracting(InvalidClientDataException::getMessage)
                .isEqualTo("Imię musi składać się z przynajmniej 2 znaków");
    }

    @Test
    void shouldDeleteClient() {
        ClientDto client = clientService.register("filip@wp.pl", "Filip");

        clientService.deleteClient(client.id());

        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientService.login("filip@wp.pl"));
    }

    @Test
    void shouldThrowWhenRegisteringWithDuplicateEmail() {
        clientService.register("filip@wp.pl", "Filip");
        assertThatExceptionOfType(EmailAlreadyExistsException.class)
                .isThrownBy(() -> clientService.register("filip@wp.pl", "Filip"))
                .extracting(EmailAlreadyExistsException::getMessage)
                .isEqualTo("Konto z adresem filip@wp.pl już istnieje.");
    }
}
