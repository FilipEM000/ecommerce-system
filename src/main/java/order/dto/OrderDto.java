package order.dto;

import java.math.BigDecimal;

public record OrderDto(Long id, String clientName, BigDecimal cost) {
}
