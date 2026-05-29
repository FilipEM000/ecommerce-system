package cart;

import entity.client.Client;
import entity.computer.Computer;
import exception.NotEnoughQuantityInMagazineException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ClientRepository;
import repository.ProductRepository;
import service.impl.CartServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {
    @Mock
    ClientRepository clientRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CartServiceImpl cartServiceImpl;

    @Test
    void shouldAddProductThrowNotEnoughQuantityInMagazineException() {
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Computer(1L, "ASUS", new BigDecimal("199.99"), 2)));

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> cartServiceImpl.addProductToCart(1L, 1L, 3))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie ma wystarczająco produktu na stanie");
    }

    @Test
    void shouldAddProductToCart() {
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Computer(1L, "ASUS", new BigDecimal("199.99"), 2)));

        cartServiceImpl.addProductToCart(1L, 1L, 2);

        assertThat(clientRepository.findById(1L).get().getCart().getProducts()).containsKey(1L);
    }

    @Test
    void shouldClearCartCorrectly() {
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Computer(1L, "ASUS", new BigDecimal("999.99"), 2)));

        cartServiceImpl.addProductToCart(1L, 1L, 2);
        cartServiceImpl.clearCart(clientRepository.findById(1L).get().getId());

        assertThat(clientRepository.findById(1L).get().getCart().getProducts()).isEmpty();
    }
}
