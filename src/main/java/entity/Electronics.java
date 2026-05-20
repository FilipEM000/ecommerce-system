package entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Electronics extends Product {

    public Electronics(long id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
    }
}
