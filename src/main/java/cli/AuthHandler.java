package cli;

import client.dto.ClientDto;
import client.service.ClientService;
import exception.ClientNotFoundException;
import exception.EmailAlreadyExistsException;
import exception.InvalidClientDataException;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public final class AuthHandler {
    private final ClientService clientService;
    private final Scanner scanner;

    public Optional<Long> handleLogin() {
        System.out.println("LOGOWANIE\nPodaj swój adres email");
        String email = scanner.nextLine();
        try {
            return Optional.of(clientService.login(email));
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ClientDto> handleRegister() {
        System.out.println("REJESTRACJA\nPodaj swój adres email");
        String email = scanner.nextLine();
        System.out.println("Podaj swoje imię");
        String name = scanner.nextLine();
        try {
            ClientDto client = clientService.register(email, name);
            System.out.println("Witaj " + client.name() + ". Twoje konto zostało utworzone.");
            return Optional.of(client);
        } catch (EmailAlreadyExistsException | InvalidClientDataException e) {
            System.out.println("Błąd rejestracji: " + e.getMessage());
            return Optional.empty();
        }
    }
}
