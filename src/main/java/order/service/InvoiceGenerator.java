package order.service;

import order.entity.Invoice;
import order.entity.Order;

public interface InvoiceGenerator {
    Invoice generateInvoice(Order order);
}
