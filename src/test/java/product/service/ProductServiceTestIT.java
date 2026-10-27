package product.service;

import exception.InvalidPriceException;
import exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.dto.ProductDto;
import product.entity.Electronics;
import product.entity.Product;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;
import product.repository.impl.InMemoryProductRepository;
import product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProductServiceTestIT {
    private ProductService productService;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        productService = new ProductServiceImpl(productRepository);
        productRepository.save(new Computer("Dell XPS 15", new BigDecimal("4999.99"), 10));
        productRepository.save(new Computer("Lenovo ThinkPad", new BigDecimal("3999.99"), 5));
        productRepository.save(new Smartphone("iPhone 15", new BigDecimal("4499.99"), 8));
        productRepository.save(new Electronics("Logitech MX Master", new BigDecimal("399.99"), 20));
    }

    @Test
    void shouldAddProductAndFindItById() {
        Electronics newProduct = new Electronics("Sony WH-1000XM5", new BigDecimal("1499.99"), 12);

        ProductDto added = productService.addProduct(newProduct);
        ProductDto found = productService.getProductById(added.id());

        assertThat(found.name()).isEqualTo("Sony WH-1000XM5");
        assertThat(found.price()).isEqualByComparingTo("1499.99");
    }

    @Test
    void shouldGetAllProducts() {
        List<ProductDto> products = productService.getAllProducts();

        assertThat(products).hasSize(4);
    }

    @Test
    void shouldFindProductsByName() {
        List<ProductDto> results = productService.getProductsByName("dell");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).name()).isEqualTo("Dell XPS 15");
    }

    @Test
    void shouldFindProductsByType() {
        List<ProductDto> computers = productService.getProductsByType("Computer");

        assertThat(computers).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsMatchName() {
        List<ProductDto> results = productService.getProductsByName("xxx");

        assertThat(results).isEmpty();
    }

    @Test
    void shouldUpdateProductPrice() {
        ProductDto product = productService.getAllProducts().get(0);

        productService.updateProductPrice(product.id(), new BigDecimal("5999.99"));
        ProductDto updated = productService.getProductById(product.id());

        assertThat(updated.price()).isEqualByComparingTo("5999.99");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceToZero() {
        ProductDto product = productService.getAllProducts().get(0);

        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> productService.updateProductPrice(product.id(), BigDecimal.ZERO))
                .extracting(InvalidPriceException::getMessage)
                .isEqualTo("Cena musi być większa od 0");
    }

    @Test
    void shouldUpdateProductQuantity() {
        ProductDto product = productService.getAllProducts().get(0);

        productService.updateProductQuantity(product.id(), 50);

        Product updatedEntity = productRepository.findById(product.id()).get();

        assertThat(updatedEntity.getQuantity()).isEqualTo(50);
    }

    @Test
    void shouldDeleteProduct() {
        ProductDto product = productService.getAllProducts().get(0);

        productService.deleteProduct(product.id());

        assertThat(productRepository.findById(product.id())).isEmpty();
        assertThat(productService.getAllProducts()).hasSize(3);
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.getProductById(product.id()));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.deleteProduct(999L));
    }
}
