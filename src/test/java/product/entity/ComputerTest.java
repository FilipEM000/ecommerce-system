package product.entity;

import org.junit.jupiter.api.Test;
import product.entity.computer.Computer;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ComputerTest {

    @Test
    void shouldCalculateTotalPriceBasedOnConfiguration() {
        Computer computer = new Computer("Dell", new BigDecimal("3000"), 5);

        computer.configure(ProcessorType.AMD_RYZEN_7, Ram.DDR5_32_5600);

        //3000 + 800 + 400
        assertThat(computer.getTotalPrice()).isEqualTo(new BigDecimal("4200"));
    }
}
