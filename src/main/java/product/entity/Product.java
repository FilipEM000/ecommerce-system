package product.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Product {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public Product(String name, BigDecimal price, Integer quantity) {
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

    public abstract String getDetails();

    public abstract ProductType getProductType();

    public abstract Product cloneProduct();
}
