package service;

import entity.Invoice;
import entity.Order;

public interface InvoiceGenerator {
    public Invoice generateInvoice(Order order);

    void printInvoice(Invoice invoice);
}
