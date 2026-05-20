package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public abstract class Product {
    private final long id;
    private final String name;
    private BigDecimal price;
    private int quantity;
}
