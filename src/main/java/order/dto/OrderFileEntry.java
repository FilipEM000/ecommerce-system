package order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderFileEntry(
        Long id,
        String clientName,
        BigDecimal cost,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime orderDate,
        List<OrderItemEntry> items) {
}
