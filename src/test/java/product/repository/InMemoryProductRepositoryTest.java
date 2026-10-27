package product.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.entity.Product;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;
import product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryProductRepositoryTest {

    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    void shouldSaveMultipleProductsWithUniqueIds() {
        Product computer = new Computer("Dell", new BigDecimal("3000"), 5);
        Product smartphone = new Smartphone("iPhone", new BigDecimal("4000"), 10);

        Product savedComputer = productRepository.save(computer);
        Product savedSmartphone = productRepository.save(smartphone);

        assertThat(savedComputer.getId()).isEqualTo(0L);
        assertThat(savedSmartphone.getId()).isEqualTo(1L);
        assertThat(productRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldRemoveProduct() {
        Product computer = new Computer("Dell", new BigDecimal("3000"), 5);
        Product savedComputer = productRepository.save(computer);

        productRepository.remove(savedComputer);

        Optional<Product> foundProduct = productRepository.findById(savedComputer.getId());
        assertThat(foundProduct).isEmpty();
        assertThat(productRepository.findAll()).isEmpty();
    }

}
