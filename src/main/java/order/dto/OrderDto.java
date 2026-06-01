package order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(Long id,
                       String clientName,
                       BigDecimal cost,
                       LocalDateTime orderDate,
                       String invoiceNumber) {
}
