import entity.computer.Computer;
import entity.Product;
import exception.InvalidPriceException;
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
import repository.ProductRepository;
import service.impl.ProductManagerImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductManagerImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductManagerImpl productManagerImpl;

    @Test
    void shouldAddProduct() {
        Computer computer = new Computer(1, "asus", new BigDecimal("999"), 10);

        productManagerImpl.addProduct(computer);

        verify(productRepository).save(computer);
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1, "asus", new BigDecimal("999"), 10)));

        productManagerImpl.deleteProduct(1);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).remove(productCaptor.capture());
    }

    @Test
    void shouldDeleteProductPriceThrowException() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productManagerImpl.deleteProduct(1));
    }

    @Test
    void shouldUpdateProductPrice() {
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1L, "name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer(1, "name", new BigDecimal("20"), 10);
        productManagerImpl.updateProductPrice(1, new BigDecimal("20"));

        var result = productRepository.findById(1).get();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateProductPriceThrowProductNotFoundException() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productManagerImpl.updateProductPrice(1, new BigDecimal("20")));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void shouldUpdateProductPriceThrowInvalidPriceException(BigDecimal price) {
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1, "name", new BigDecimal("199.99"), 10)));

        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> productManagerImpl.updateProductPrice(1, price));
    }

    private static Stream<Arguments> testData(){
        return Stream.of(
                arguments(new BigDecimal("0")),
                arguments(new BigDecimal("-2")),
                arguments((BigDecimal)null)
        );
    }

    @Test
    void shouldUpdateProductQuantity() {
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1L, "name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer(1, "name", new BigDecimal("199.99"), 15);
        productManagerImpl.updateProductQuantity(1, 15);

        var result = productRepository.findById(1).get();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateProductQuantityThrowProductNotFoundException() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productManagerImpl.updateProductQuantity(1, 5));
    }
}
