package entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Computer extends Product {
    private String processor;
    private int ram;

    public Computer(long id, String name, BigDecimal price, int quantity, String processor, int ram) {
        super(id, name, price, quantity);
        this.processor = processor;
        this.ram = ram;
    }

    public void configure(String processor, int ram) {
        this.processor = processor;
        this.ram = ram;
    }
}
