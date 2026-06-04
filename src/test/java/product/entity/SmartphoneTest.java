package product.entity;

import org.junit.jupiter.api.Test;
import product.entity.smartphone.BatteryCapacity;
import product.entity.smartphone.Color;
import product.entity.smartphone.Smartphone;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SmartphoneTest {

    @Test
    void shouldCalculateTotalPriceBasedOnBattery() {
        Smartphone phone = new Smartphone("iPhone 15", new BigDecimal("4000"), 10);

        phone.configure(Color.BLACK, BatteryCapacity.LARGE, new HashSet<>());

        assertThat(phone.getTotalPrice()).isEqualTo(new BigDecimal("4100"));
    }
}
