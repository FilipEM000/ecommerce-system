package entity.computer;

import entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Computer extends Product {
    private ProcessorType processor;
    private Ram ram;

    public Computer(Long id, String name, BigDecimal price, Integer quantity) {
        super(id, name, price, quantity);
        this.processor = ProcessorType.AMD_RYZEN_5;
        this.ram = new Ram(16, 3200, 2);
    }

    public void configure(ProcessorType processor, Ram ram) {
        this.processor = processor;
        this.ram = ram;
    }
}
