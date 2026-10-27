package order.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderDto(Long id,
                       String clientName,
                       BigDecimal cost,
                       ZonedDateTime orderDate,
                       String invoiceNumber) {
}
