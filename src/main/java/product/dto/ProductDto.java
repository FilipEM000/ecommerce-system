package product.dto;

import product.entity.ProductType;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price, ProductType type, String details) {
}
