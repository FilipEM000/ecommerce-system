package repository;

import entity.Invoice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InvoiceRepository {
    private Map<String, Invoice> invoices = new HashMap<>();

    public Invoice save(Invoice invoice) {
        invoices.putIfAbsent(invoice.invoiceNumber(), invoice);
        return invoice;
    }

    public Optional<Invoice> findByNumber(String invoiceNumber) {
        return Optional.ofNullable(invoices.get(invoiceNumber));
    }
}
