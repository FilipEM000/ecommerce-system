package client.service;

import client.dto.AddToCartRequest;
import client.entity.Client;
import client.repository.ClientRepository;
import product.dto.ComputerConfiguration;
import product.entity.computer.Computer;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import exception.NotEnoughQuantityInMagazineException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.repository.ProductRepository;
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
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("199.99"), 2)));

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> cartServiceImpl.addStandardProductToCart(new AddToCartRequest(1L, 1L, 3)))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie masz wystarczającej ilości produktu na stanie. Dostępnych w magazynie: 2, w koszyku masz już: 0, próbujesz dodać: 3");
    }

    @Test
    void shouldAddProductToCart() {
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("199.99"), 2)));

        cartServiceImpl.addStandardProductToCart(new AddToCartRequest(1L, 1L, 2));

        assertThat(clientRepository.findById(1L).get().getCart().getProducts())
                .containsKey(new Computer("ASUS", new BigDecimal("199.99"), 2));
    }

    @Test
    void shouldAddComputerToCartAndConfigureIt() {
        Client client = new Client("Filip", "filip@wp.pl");
        client.setId(1L);
        Computer masterComputer = new Computer("ASUS", new BigDecimal("100"), 5);
        masterComputer.setId(1L);
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(masterComputer));
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(client));


        cartServiceImpl.addComputerToCart(
                new AddToCartRequest(1L, 1L, 2),
                new ComputerConfiguration("AMD_RYZEN_7", "DDR4_16_3200")
        );
        Computer expectedComputer = new Computer("ASUS", new BigDecimal("100"), 5);
        expectedComputer.setId(1L);
        expectedComputer.configure(ProcessorType.AMD_RYZEN_7, Ram.DDR4_16_3200);

        assertThat(client.getCart().getProducts()).containsKey(expectedComputer);
        assertThat(masterComputer.getProcessor()).isEqualTo(ProcessorType.INTEL_CORE_I5);
        assertThat(masterComputer.getRam()).isEqualTo(Ram.DDR4_8_2400);
    }

    @Test
    void shouldClearCartCorrectly() {
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(new Client("Filip", "filip.ostrowicki@wp.pl")));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("999.99"), 2)));

        cartServiceImpl.addStandardProductToCart(new AddToCartRequest(1L, 1L, 2));
        cartServiceImpl.clearCart(clientRepository.findById(1L).get().getId());

        assertThat(clientRepository.findById(1L).get().getCart().getProducts()).isEmpty();
    }
}
