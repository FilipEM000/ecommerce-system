package cli;

import client.dto.ClientDto;
import lombok.RequiredArgsConstructor;

import java.util.InputMismatchException;
import java.util.Scanner;

@RequiredArgsConstructor
public final class ConsoleApp {
    private final AuthHandler authHandler;
    private final ProductHandler productHandler;
    private final CartHandler cartHandler;
    private final OrderHandler orderHandler;
    private final Scanner scanner;
    private Long currentClientId;

    public void run() {
        int option = -1;
        do {
            try {
                System.out.println("1 - zaloguj się\n2 - stwórz nowe konto\n0 - wyjdź z programu");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> currentClientId = authHandler.handleLogin();
                    case 2 -> {
                        ClientDto client = authHandler.handleRegister();
                        currentClientId = client.id();
                    }
                    case 0 -> System.out.println("Do zobaczenia!");
                    default -> System.out.println("Nieznana opcja.");
                }

                if (currentClientId != null) {
                    shopMenuLoop();
                }

            } catch (InputMismatchException e) {
                System.out.println("Wpisano nieprawidłowy znak");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Wystąpił krytyczny błąd: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void shopMenuLoop() {
        int shopOption = -1;
        do {
            try {
                printMenu();
                shopOption = scanner.nextInt();
                scanner.nextLine();

                switch (shopOption) {
                    case 1 -> productHandler.productsMenu();
                    case 2 -> cartHandler.handleAddToCart(currentClientId);
                    case 3 -> cartHandler.printProductsInCart(currentClientId);
                    case 4 -> orderHandler.handlePlaceOrder(currentClientId);
                    case 0 -> {
                        System.out.println("Wylogowano pomyślnie");
                        currentClientId = null;
                    }
                    default -> System.out.println("Nieznana opcja");
                }
            } catch (InputMismatchException e) {
                System.err.println("Wpisano nieprawidłowy znak");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Wystąpił błąd w sklepie: " + e.getMessage());
            }
        } while (shopOption != 0 && currentClientId != null);
    }

    private void printMenu() {
        System.out.println("""
                1 - zobacz produkty
                2 - dodaj do koszyka
                3 - wyświetl koszyk
                4 - złóż zamówienie
                0 - wyloguj się
                """);
    }
}
