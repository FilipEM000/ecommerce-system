package product.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price, String type, String details) {
}
