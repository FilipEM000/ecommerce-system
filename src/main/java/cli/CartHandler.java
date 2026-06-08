package cli;

import client.dto.AddToCartRequest;
import client.service.CartService;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import product.dto.ComputerConfiguration;
import product.dto.ProductDto;
import product.dto.SmartphoneConfiguration;
import product.entity.Product;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import product.entity.smartphone.BatteryCapacity;
import product.entity.smartphone.Color;
import product.service.ProductService;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

@AllArgsConstructor
public class CartHandler {
    private final CartService cartService;
    private final ProductService productService;
    private final Scanner scanner;

    public void handleAddToCart(Long clientId) {
        System.out.println("Podaj id produktu, który chcesz dodać do koszyka");
        Long productId = scanner.nextLong();
        scanner.nextLine();
        try {
            ProductDto product = productService.getProductById(productId);
            System.out.println("Podaj ilość, którą chcesz dodać:");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            AddToCartRequest request = new AddToCartRequest(clientId, productId, quantity);
            switch (product.type()) {
                case "Computer" -> addComputerToCart(request);
                case "Smartphone" -> addSmartphoneToCart(request);
                default -> {
                    cartService.addStandardProductToCart(request);
                    System.out.println("Dodano produkt do koszyka!");
                }
            }
        } catch (ProductNotFoundException | NotEnoughQuantityInMagazineException |
                 IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printProductsInCart(Long clientId) {
        Map<Product, Integer> products = cartService.getAllProductsInCart(clientId).getProducts();
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
    private void addComputerToCart(AddToCartRequest request) {
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

        cartService.addComputerToCart(request, new ComputerConfiguration(processor, ram));
        System.out.println("Dodano skonfigurowany komputer do koszyka!");
    }
    private void addSmartphoneToCart(AddToCartRequest request) {
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

        cartService.addSmartphoneToCart(request, new SmartphoneConfiguration(color, battery));
        System.out.println("Dodano skonfigurowanego smartfona do koszyka!");
    }
}
