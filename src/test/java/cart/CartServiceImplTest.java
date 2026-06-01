package cart;

import client.entity.Client;
import product.entity.computer.Computer;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import exception.NotEnoughQuantityInMagazineException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import client.repository.impl.InMemoryClientRepository;
import product.repository.impl.InMemoryProductRepository;
import client.service.impl.CartServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {
    @Mock
    InMemoryClientRepository inMemoryClientRepository;

    @Mock
    InMemoryProductRepository inMemoryProductRepository;

    @InjectMocks
    CartServiceImpl cartServiceImpl;

    @Test
    void shouldAddProductThrowNotEnoughQuantityInMagazineException() {
        when(inMemoryClientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(inMemoryProductRepository.findById(any()))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("199.99"), 2)));

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> cartServiceImpl.addStandardProductToCart(1L, 1L, 3))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie ma wystarczająco produktu na stanie");
    }

    @Test
    void shouldAddProductToCart() {
        when(inMemoryClientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(inMemoryProductRepository.findById(any()))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("199.99"), 2)));

        cartServiceImpl.addStandardProductToCart(1L, 1L, 2);

        assertThat(inMemoryClientRepository.findById(1L).get().getCart().getProducts())
                .containsKey(new Computer("ASUS", new BigDecimal("199.99"), 2));
    }

    @Test
    void shouldAddComputerToCartAndConfigureIt() {
        Client client = new Client("Filip", "filip@wp.pl");
        client.setId(1L);
        Computer masterComputer = new Computer("ASUS", new BigDecimal("100"), 5);
        masterComputer.setId(1L);
        when(inMemoryProductRepository.findById(any()))
                .thenReturn(Optional.of(masterComputer));
        when(inMemoryClientRepository.findById(any()))
                .thenReturn(Optional.of(client));


        cartServiceImpl.addComputerToCart(1L, 1L, 2, "AMD_RYZEN_7", "DDR4_16_3200");
        Computer expectedComputer = new Computer("ASUS", new BigDecimal("100"), 5);
        expectedComputer.setId(1L);
        expectedComputer.configure(ProcessorType.AMD_RYZEN_7, Ram.DDR4_16_3200);

        assertThat(client.getCart().getProducts()).containsKey(expectedComputer);
        assertThat(masterComputer.getProcessor()).isEqualTo(ProcessorType.INTEL_CORE_I5);
        assertThat(masterComputer.getRam()).isEqualTo(Ram.DDR4_8_2400);
    }

    @Test
    void shouldClearCartCorrectly() {
        when(inMemoryClientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(inMemoryProductRepository.findById(any()))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("999.99"), 2)));

        cartServiceImpl.addStandardProductToCart(1L, 1L, 2);
        cartServiceImpl.clearCart(inMemoryClientRepository.findById(1L).get().getId());

        assertThat(inMemoryClientRepository.findById(1L).get().getCart().getProducts()).isEmpty();
    }
}
