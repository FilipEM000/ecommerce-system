package order.repository.impl;

import order.entity.Invoice;
import order.repository.InvoiceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryInvoiceRepository implements InvoiceRepository {
    private Map<String, Invoice> invoices = new HashMap<>();

    public Invoice save(Invoice invoice) {
        invoices.put(invoice.invoiceNumber(), invoice);
        return invoice;
    }

    public Optional<Invoice> findByNumber(String invoiceNumber) {
        return Optional.ofNullable(invoices.get(invoiceNumber));
    }
}
