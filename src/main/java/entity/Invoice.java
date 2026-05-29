package entity;

import java.time.LocalDate;

public record Invoice(String invoiceNumber, Order order) {
    public Invoice (Order order) {
        this(getInvoiceNumber(order), order);
    }

    private static String getInvoiceNumber (Order order) {
        return "FV/" + LocalDate.now().getYear() + "/" + order.getId();
    }
}
