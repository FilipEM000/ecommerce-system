package product.entity.computer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import product.entity.Product;
import product.entity.ProductType;
import product.validator.ProductValidator;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
public final class Computer extends Product {
    private ProcessorType processor;
    private Ram ram;

    public Computer(String name, BigDecimal price, int quantity) {
        super(name, price, quantity);
        this.processor = ProcessorType.INTEL_CORE_I5;
        this.ram = Ram.DDR4_8_2400;
    }

    public Computer(Computer source) {
        super(source);
        this.processor = source.processor;
        this.ram = source.ram;
    }

    public void configure(ProcessorType processor, Ram ram) {
        ProductValidator.validateComputerConfiguration(processor, ram);
        this.processor = processor;
        this.ram = ram;

        BigDecimal newPrice = getPrice()
                .add(processor.getAdditionalCost())
                .add(ram.getAdditionalCost());
        setPrice(newPrice);
    }

    @Override
    public String getDetails() {
        return "processor: " + processor + ", ram:  " + ram;
    }

    @Override
    public ProductType getProductType() {
        return ProductType.COMPUTER;
    }

    @Override
    public Product cloneProduct() {
        return new Computer(this);
    }
}
