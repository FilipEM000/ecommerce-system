import entity.computer.Computer;
import entity.Product;
import exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProductRepository;
import service.impl.ProductManagerImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductManagerImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductManagerImpl productManagerImpl;

    @Test
    void shouldAddProduct(){
        Computer computer = new Computer(1, "asus", new BigDecimal("999"), 10);

        productManagerImpl.addProduct(computer);

        verify(productRepository).save(computer);
    }

    @Test
    void shouldDeleteProduct(){
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1, "asus", new BigDecimal("999"), 10)));

        productManagerImpl.deleteProduct(1);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).remove(productCaptor.capture());
    }

    @Test
    void shouldDeleteProductThrowException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productManagerImpl.deleteProduct(1));
    }

    @Test
    void shouldUpdateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1L, "name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer(1, "name", new BigDecimal("20"), 5);
        productManagerImpl.updateProduct(1, new BigDecimal("20"), 5);

        var result = productRepository.findById(1).get();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldUpdateProductThrowProductNotFoundException(){
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productManagerImpl.updateProduct(1, new BigDecimal("20"), 5));
    }

    @Test
    void shouldUpdateProductUpdateOnlyPrice(){
        when(productRepository.findById(1)).thenReturn(Optional.of(
                new Computer(1L, "name", new BigDecimal("199.99"), 10)));

        var expectedResult = new Computer(1, "name", new BigDecimal("20"), 10);
        productManagerImpl.updateProduct(1, new BigDecimal("20"), -1);

        var result = productRepository.findById(1).get();

        assertThat(result).isEqualTo(expectedResult);
    }
}
