package cli;

import client.dto.ClientDto;
import client.service.ClientService;
import exception.ClientNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public final class AuthHandler {
    private final ClientService clientService;
    private final Scanner scanner;

    public Long handleLogin() {
        System.out.println("LOGOWANIE\nPodaj swój adres email");
        String email = scanner.nextLine();
        try {
            return clientService.login(email);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ClientDto handleRegister() {
        System.out.println("REJESTRACJA\nPodaj swój adres email");
        String email = scanner.nextLine();
        System.out.println("Podaj swoje imię");
        String name = scanner.nextLine();
        ClientDto client = clientService.register(email, name);
        System.out.println("Witaj " + client.name() + ". Twoje konto zostało utworzone.");
        return client;
    }
}
