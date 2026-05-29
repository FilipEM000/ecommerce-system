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
        this.processor = ProcessorType.INTEL_CORE_I5;
        this.ram = Ram.DDR4_8_2400;
    }

    public void configure(ProcessorType processor, Ram ram) {
        this.processor = processor;
        this.ram = ram;
    }

    @Override
    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = BigDecimal.ZERO;

        totalPrice = totalPrice.add(processor.getAdditionalCost());
        totalPrice = totalPrice.add(ram.getAdditionalCost());

        return super.getTotalPrice().add(totalPrice);
    }

    @Override
    public String getDetails() {
        return "processor: " + processor + ", ram:  " + ram;
    }

    @Override
    public String getProductType() {
        return "Computer";
    }
}
