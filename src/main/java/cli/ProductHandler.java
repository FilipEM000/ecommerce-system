package cli;

import lombok.AllArgsConstructor;
import product.dto.ProductDto;
import product.service.ProductService;

import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
public final class ProductHandler {
    private final ProductService productService;
    private final Scanner scanner;

    public void productsMenu() {
        System.out.println("""
                1 - pokaż wszystkie produkty
                2 - Szukaj produktu po nazwie
                3 - filtruj po typie
                0 - powrót do menu
                """);
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1 -> {
                List<ProductDto> products = productService.getAllProducts();
                if (products.isEmpty()) {
                    System.out.println("Brak dostępnych produktów.");
                } else {
                    products.forEach(System.out::println);
                }
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
            default -> System.out.println("Nieprawidłowa opcja. Wybierz liczbę od 0 do 3.");
        }
    }
}
