package cli;

import exception.ClientNotFoundException;
import exception.EmptyCartException;
import exception.InvalidPromoCodeException;
import exception.NotEnoughQuantityInMagazineException;
import lombok.AllArgsConstructor;
import order.dto.OrderDto;
import order.service.OrderService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@AllArgsConstructor
public final class OrderHandler {
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z");

    private final OrderService orderService;
    private final Scanner scanner;

    public void handlePlaceOrder(Long clientId) {
        try {
            System.out.println("Czy masz kod rabatowy? (wpisz kod lub wciśnij ENTER, by pominąć):");
            String promoCode = scanner.nextLine().trim();

            if (promoCode.isEmpty()) {
                promoCode = null;
            }

            OrderDto order = orderService.placeOrder(clientId, promoCode);
            String formattedDate = order.orderDate().format(DATE_TIME_FORMATTER);
            printInvoice(order, formattedDate);
        } catch (EmptyCartException | ClientNotFoundException |
                 NotEnoughQuantityInMagazineException | InvalidPromoCodeException e) {
            System.out.println("Błąd składania zamówienia: " + e.getMessage());
        }
    }

    private void printInvoice(OrderDto order, String formattedDate) {
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
