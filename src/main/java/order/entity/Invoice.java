package order.entity;

import config.TimeConfig;

import java.time.ZonedDateTime;

public record Invoice(String invoiceNumber, Order order) {
    public Invoice(Order order) {
        this(getInvoiceNumber(order), order);
    }

    private static String getInvoiceNumber(Order order) {
        return "FV/" + ZonedDateTime.now(TimeConfig.APP_ZONE).getYear() + "/" + order.getId();
    }
}
