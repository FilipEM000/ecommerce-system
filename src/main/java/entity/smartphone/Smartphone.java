package entity.smartphone;

import entity.Product;
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

    public Smartphone(long id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
        this.color = Color.BLACK;
        this.batteryCapacity = BatteryCapacity.STANDARD;
        this.accessories = new HashSet<>();
    }

    public void configure(Color color, BatteryCapacity batteryCapacity, Set<Accessory> accessories) {
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;
    }
}
