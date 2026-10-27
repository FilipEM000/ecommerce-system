package product.mapper;

import org.junit.jupiter.api.Test;
import product.dto.ProductDto;
import product.entity.Product;
import product.entity.ProductType;
import product.entity.computer.Computer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    @Test
    void shouldMapProductToDto() {
        Computer computer = new Computer("Dell XPS", new BigDecimal("5000"), 10);
        computer.setId(1L);

        ProductDto productDto = ProductMapper.mapToDto(computer);

        assertThat(productDto).isEqualTo(new ProductDto(
                1L,
                "Dell XPS",
                new BigDecimal("5000"),
                ProductType.COMPUTER,
                "processor: INTEL_CORE_I5, ram:  DDR4_8_2400"));
    }
}
