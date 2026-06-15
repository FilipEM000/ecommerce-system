package order.repository.impl;

import order.entity.Invoice;
import order.repository.InvoiceRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryInvoiceRepository implements InvoiceRepository {
    private final Map<String, Invoice> invoices = new ConcurrentHashMap<>();

    public Invoice save(Invoice invoice) {
        invoices.put(invoice.invoiceNumber(), invoice);
        return invoice;
    }

    public Optional<Invoice> findByNumber(String invoiceNumber) {
        return Optional.ofNullable(invoices.get(invoiceNumber));
    }
}
