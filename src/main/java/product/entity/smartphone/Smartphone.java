package product.entity.smartphone;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import product.entity.Product;
import product.entity.ProductType;
import product.validator.ProductValidator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
public final class Smartphone extends Product {
    private Color color;
    private BatteryCapacity batteryCapacity;
    private Set<Accessory> accessories;

    public Smartphone(String name, BigDecimal price, Integer quantity) {
        super(name, price, quantity);
        this.color = Color.BLACK;
        this.batteryCapacity = BatteryCapacity.SMALL;
        this.accessories = new HashSet<>();
    }

    public Smartphone(Smartphone source) {
        super(source);
        this.color = source.color;
        this.batteryCapacity = source.batteryCapacity;
        this.accessories = new HashSet<>(source.accessories);
    }

    public void configure(Color color, BatteryCapacity batteryCapacity, Set<Accessory> accessories) {
        ProductValidator.validateSmartphoneConfiguration(color, batteryCapacity);
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;

        BigDecimal newPrice = getPrice().add(batteryCapacity.getAdditionalCost());
        for (Accessory accessory : accessories) {
            newPrice = newPrice.add(accessory.getAdditionalCost());
        }
        setPrice(newPrice);
    }

    @Override
    public String getDetails() {
        return "color: " + color + ",battery capacity: " + batteryCapacity + ",accessories: " + accessories;
    }

    @Override
    public ProductType getProductType() {
        return ProductType.SMARTPHONE;
    }

    @Override
    public Product cloneProduct() {
        return new Smartphone(this);
    }
}
