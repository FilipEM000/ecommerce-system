package order.repository;

import order.entity.Invoice;

import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);

    Optional<Invoice> findByNumber(String invoiceNumber);
}
