package order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import order.dto.OrderFileEntry;
import order.dto.OrderItemEntry;
import order.entity.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFileWriter {
    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private final List<OrderFileEntry> orders = new ArrayList<>();

    public OrderFileWriter(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Writes an order to a JSON file.
     * This method is synchronized to ensure thread safety, preventing race conditions
     * and file corruption when multiple clients place orders simultaneously.
     *
     * @param order The order entity to be serialized and saved.
     */
    public synchronized void write(Order order) {
        List<OrderItemEntry> items = order.getProducts().entrySet().stream()
                .map(entry -> new OrderItemEntry(
                        entry.getKey().getName(),
                        entry.getKey().getProductType(),
                        entry.getKey().getTotalPrice(),
                        entry.getValue()))
                .toList();

        orders.add(new OrderFileEntry(
                order.getId(),
                order.getClient().getName(),
                order.getCost(),
                order.getOrderDate(),
                items));
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), orders);
        } catch (IOException e) {
            System.err.println("Błąd zapisu zamówienia do pliku | " + e.getMessage());
        }
    }
}
