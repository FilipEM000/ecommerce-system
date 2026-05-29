package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public abstract class Product {
    private final Long id;
    private final String name;
    private BigDecimal price;
    private int quantity;

    public BigDecimal getTotalPrice() {
        return price;
    }

    public abstract String getDetails();

    public abstract String getProductType();
}
