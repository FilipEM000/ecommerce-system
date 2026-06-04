package order.dto;

import java.math.BigDecimal;

public record OrderItemEntry(String name, String type, BigDecimal price, int quantity) {
}
