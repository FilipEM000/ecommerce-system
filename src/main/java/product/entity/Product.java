package product.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int quantity;

    public Product(String name, BigDecimal price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(Product source) {
        this.id = source.id;
        this.name = source.name;
        this.price = source.price;
        this.quantity = source.quantity;
    }

    public BigDecimal getTotalPrice() {
        return price;
    }

    public abstract String getDetails();

    public abstract String getProductType();

    public abstract Product cloneProduct();
}
