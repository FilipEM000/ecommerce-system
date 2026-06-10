package product.service;

import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.entity.Product;
import product.entity.computer.Computer;
import product.repository.ProductRepository;
import product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @Test
    void shouldAddProduct() {
        Computer computer = new Computer("asus", new BigDecimal("999"), 10);
        when(productRepository.save(any())).thenReturn(computer);

        productServiceImpl.addProduct(computer);

        verify(productRepository).save(computer);
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Computer("asus", new BigDecimal("999"), 10)));

        productServiceImpl.deleteProduct(1L);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).remove(productCaptor.capture());
    }

    @Test
    void shouldDeleteProductPriceThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productServiceImpl.deleteProduct(1L));
    }

    @Test
    void shouldUpdateProductPrice() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Computer("name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer("name", new BigDecimal("20"), 10);
        productServiceImpl.updateProductPrice(1L, new BigDecimal("20"));

        var result = productRepository.findById(1L).get();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateProductPriceThrowProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productServiceImpl.updateProductPrice(1L, new BigDecimal("20")));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void shouldUpdateProductPriceThrowInvalidPriceException(BigDecimal price) {
        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> productServiceImpl.updateProductPrice(1L, price));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                arguments(new BigDecimal("0")),
                arguments(new BigDecimal("-2")),
                arguments((BigDecimal) null)
        );
    }

    @Test
    void shouldUpdateProductQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Computer("name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer("name", new BigDecimal("199.99"), 15);
        productServiceImpl.updateProductQuantity(1L, 15);

        var result = productRepository.findById(1L).get();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateProductQuantityThrowProductNotFoundException() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productServiceImpl.updateProductQuantity(1L, 5));
    }

    @Test
    void shouldUpdateProductQuantityThrowInvalidQuantityException() {
        assertThatExceptionOfType(InvalidQuantityException.class)
                .isThrownBy(() -> productServiceImpl.updateProductQuantity(1L, -1))
                .extracting(InvalidQuantityException::getMessage)
                .isEqualTo("Dostępna ilość produktu nie może być mniejsza niż 0");
    }
}
