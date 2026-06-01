package order.service.impl;

import lombok.AllArgsConstructor;
import order.entity.Invoice;
import order.entity.Order;
import order.repository.InvoiceRepository;
import order.service.InvoiceGenerator;

import java.time.LocalDate;

@AllArgsConstructor
public class InvoiceGeneratorImpl implements InvoiceGenerator {
    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice generateInvoice(Order order) {
        Invoice invoice = new Invoice(order);
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public void printInvoice(Invoice invoice) {
        System.out.println("========================================");
        System.out.println("FAKTURA VAT NR: " + invoice.invoiceNumber());
        System.out.println("Data wystawienia: " + LocalDate.now());
        System.out.println("Nabywca: " + invoice.order().getClient().getName());
        System.out.println("----------------------------------------");

        System.out.println("----------------------------------------");
        System.out.println("DO ZAPŁATY: " + invoice.order().getCost() + " PLN");
        System.out.println("========================================");
    }
}
