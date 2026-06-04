package cli;

import client.dto.AddToCartRequest;
import client.dto.ClientDto;
import client.service.CartService;
import client.service.ClientService;
import exception.ClientNotFoundException;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import order.dto.OrderDto;
import order.service.InvoiceGenerator;
import order.service.OrderService;
import product.dto.ComputerConfiguration;
import product.dto.ProductDto;
import product.dto.SmartphoneConfiguration;
import product.entity.Product;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import product.entity.smartphone.BatteryCapacity;
import product.entity.smartphone.Color;
import product.service.ProductService;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
public final class ConsoleApp {
    private final CartService cartService;
    private final ClientService clientService;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderService orderService;
    private final ProductService productService;

    private Scanner scanner = new Scanner(System.in);
    private Long currentClientId;

    public void run() {
        int option = -1;
        do {
            try {
                System.out.println("1 - zaloguj się\n2 - stwórz nowe konto\n0 - wyjdź z programu");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> handleLogin();
                    case 2 -> handleRegister();
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

    private void printMenu() {
        System.out.println("""
                1 - zobacz produkty
                2 - dodaj do koszyka
                3 - wyświetl koszyk
                4 - złóż zamówienie
                0 - wyloguj się
                """);
    }

    private void productsMenu() {
        System.out.println("""
                1 - pokaż wszystkie produkty
                2 - Szukaj produktu po nazwie
                3 - filtruj po typie
                0 - powrót do menu
                """);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> {
                List<ProductDto> products = productService.getAllProducts();
                products.forEach(System.out::println);
            }
            case 2 -> {
                System.out.println("Podaj nazwę produktu");
                String name = scanner.nextLine();
                List<ProductDto> products = productService.getProductsByName(name);
                if (products.isEmpty()) {
                    System.out.println("Nie znaleziono produktów o nazwie: " + name);
                } else {
                    products.forEach(System.out::println);
                }
            }
            case 3 -> {
                System.out.println("Podaj typ produktu");
                String type = scanner.nextLine();
                List<ProductDto> products = productService.getProductsByType(type);
                if (products.isEmpty()) {
                    System.out.println("Nie znaleziono produktów o typie " + type);
                } else {
                    products.forEach(System.out::println);
                }
            }
        }
    }

    private void shopMenuLoop() {
        int shopOption = -1;
        do {
            try {
                printMenu();
                shopOption = scanner.nextInt();
                scanner.nextLine();

                switch (shopOption) {
                    case 1 -> productsMenu();
                    case 2 -> handleAddToCart();
                    case 3 -> printProductsInCart();
                    case 4 -> handlePlaceOrder();
                    case 0 -> {
                        System.out.println("Wylogowano pomyślnie");
                        currentClientId = null;
                    }
                    default -> System.out.println("Nieznana opcja");
                }
            } catch (InputMismatchException e) {
                System.out.println("Wpisano nieprawidłowy znak");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Wystąpił błąd w sklepie: " + e.getMessage());
            }
        } while (shopOption != 0 && currentClientId != null);
    }

    private void handleLogin() {
        System.out.println("LOGOWANIE\nPodaj swój adres email");
        String email = scanner.nextLine();
        try {
            currentClientId = clientService.login(email);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRegister() {
        System.out.println("REJESTRACJA\nPodaj swój adres email");
        String email = scanner.nextLine();
        System.out.println("Podaj swoje imię");
        String name = scanner.nextLine();
        ClientDto client = clientService.register(email, name);
        currentClientId = client.id();
        System.out.println("Witaj " + client.name() + ". Twoje konto zostało utworzone.");
    }

    private void handleAddToCart() {
        System.out.println("Podaj id produktu, który chcesz dodać do koszyka");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        try {
            ProductDto product = productService.getProductById(productId);

            System.out.println("Podaj ilość, którą chcesz dodać:");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            switch (product.type()) {
                case "Computer" -> addComputerToCart(product.id(), quantity);
                case "Smartphone" -> addSmartphoneToCart(product.id(), quantity);
                default -> {
                    cartService.addStandardProductToCart(new AddToCartRequest(currentClientId, productId, quantity));
                    System.out.println("Dodano produkt do koszyka!");
                }
            }
        } catch (ProductNotFoundException | NotEnoughQuantityInMagazineException |
                 IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handlePlaceOrder() {
        try {
            OrderDto order = orderService.placeOrder(currentClientId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = order.orderDate().format(formatter);
            printInvoice(order, formattedDate);
        } catch (Exception e) {
            System.out.println("Błąd składania zamówienia: " + e.getMessage());
        }
    }

    private void printProductsInCart() {
        Map<Product, Integer> products = cartService.getAllProductsInCart(currentClientId).getProducts();

        if (products.isEmpty()) {
            System.out.println("Twój koszyk jest pusty");
        } else {
            System.out.println("Twój koszyk:");
            products.forEach((product, quantity) -> {
                System.out.println(product.getName()
                        + " (" + product.getProductType()
                        + ") - Ilość: " + quantity + " szt. || "
                        + product.getDetails());
                System.out.println("   Cena za sztukę: " + product.getTotalPrice() + " PLN");
            });
        }
    }

    void addComputerToCart(Long productId, int quantity) {
        System.out.println("--- KONFIGURACJA KOMPUTERA ---");
        System.out.println("Dostępne procesory:");
        Arrays.stream(ProcessorType.values())
                .forEach(processorType -> System.out.println(processorType.name()));
        System.out.print("Wpisz procesor: ");
        String processor = scanner.nextLine().toUpperCase();
        System.out.println("Dostępny RAM:");
        Arrays.stream(Ram.values())
                .forEach(ram -> System.out.println(ram.name()));
        System.out.print("Wpisz RAM: ");
        String ram = scanner.nextLine().toUpperCase();
        cartService.addComputerToCart(
                new AddToCartRequest(currentClientId, productId, quantity), new ComputerConfiguration(processor, ram));
        System.out.println("Dodano skonfigurowany komputer do koszyka!");
    }

    void addSmartphoneToCart(Long productId, int quantity) {
        System.out.println("--- KONFIGURACJA SMARTFONA ---");
        System.out.println("Dostępne kolory:");
        Arrays.stream(Color.values())
                .forEach(color -> System.out.println(color.name()));
        System.out.print("Wpisz kolor: ");
        String color = scanner.nextLine().toUpperCase();
        System.out.println("Dostępne baterie:");
        Arrays.stream(BatteryCapacity.values())
                .forEach(batteryCapacity -> System.out.println(batteryCapacity.name()));
        System.out.print("Wpisz baterię: ");
        String battery = scanner.nextLine().toUpperCase();
        cartService.addSmartphoneToCart(
                new AddToCartRequest(currentClientId, productId, quantity), new SmartphoneConfiguration(color, battery));
        System.out.println("Dodano skonfigurowanego smartfona do koszyka!");
    }

    public void printInvoice(OrderDto order, String formattedDate) {
        System.out.println("\n========================================");
        System.out.println("ZAMÓWIENIE ZŁOŻONE POMYŚLNIE!");
        System.out.println("FAKTURA VAT NR: " + order.invoiceNumber());
        System.out.println("Data zamówienia: " + formattedDate);
        System.out.println("Nabywca: " + order.clientName());
        System.out.println("----------------------------------------");
        System.out.println("DO ZAPŁATY: " + order.cost() + " PLN");
        System.out.println("========================================\n");
    }
}
