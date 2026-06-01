package order.service;

import order.entity.Invoice;
import order.entity.Order;

public interface InvoiceGenerator {
    public Invoice generateInvoice(Order order);

    void printInvoice(Invoice invoice);
}
