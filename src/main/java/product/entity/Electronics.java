package product.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Electronics extends Product {

    public Electronics(String name, BigDecimal price, Integer quantity) {
        super(name, price, quantity);
    }

    public Electronics(Electronics source) {
        super(source);
    }

    @Override
    public String getDetails() {
        return "none";
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }

    @Override
    public Electronics cloneProduct() {
        return new Electronics(this);
    }
}
