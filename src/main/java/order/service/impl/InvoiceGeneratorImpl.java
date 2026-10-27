package order.service.impl;

import lombok.AllArgsConstructor;
import order.entity.Invoice;
import order.entity.Order;
import order.repository.InvoiceRepository;
import order.service.InvoiceGenerator;

import java.time.LocalDate;

@AllArgsConstructor
public final class InvoiceGeneratorImpl implements InvoiceGenerator {
    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice generateInvoice(Order order) {
        Invoice invoice = new Invoice(order);
        invoiceRepository.save(invoice);
        return invoice;
    }
}
