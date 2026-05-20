package entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
public class Smartphone extends Product {
    private String color;
    private int batteryCapacity;
    private List<String> accessories;

    public Smartphone(long id, String name, BigDecimal price, int quantity, String color, int batteryCapacity, List<String> accessories) {
        super(id, name, price, quantity);
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;
    }

    public void configure(String color, int batteryCapacity, List<String> accessories) {
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;
    }
}
