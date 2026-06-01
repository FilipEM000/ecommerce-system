package product.entity.smartphone;

import product.entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Smartphone extends Product {
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
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;
    }

    @Override
    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = BigDecimal.ZERO;

        totalPrice = totalPrice.add(batteryCapacity.getAdditionalCost());
        for (Accessory accessory : accessories) {
            totalPrice = totalPrice.add(accessory.getAdditionalCost());
        }

        return super.getTotalPrice().add(totalPrice);
    }

    @Override
    public String getDetails() {
        return "color: " + color + ",battery capacity: " + batteryCapacity + ",accessories: " + accessories;
    }

    @Override
    public String getProductType() {
        return "Smartphone";
    }

    @Override
    public Product cloneProduct() {
        return new Smartphone(this);
    }
}
