package order;

import entity.Order;
import entity.client.Client;
import entity.computer.Computer;
import entity.smartphone.Smartphone;
import exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ClientRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import service.impl.CartServiceImpl;
import service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CartServiceImpl cartService;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void shouldPlaceOrder() {
        Client client = new Client("Filip", "filip.ostrowicki@wp.pl");
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(new Computer(1L, "ASUS", new BigDecimal("999.99"), 2)));
        when(productRepository.findById(2L))
                .thenReturn(Optional.of(new Smartphone(2L, "iPhone", new BigDecimal("1999.99"), 2)));

        client.getCart().getProducts().put(1L, 2);
        client.getCart().getProducts().put(2L, 2);
        var result = orderService.placeOrder(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        new Order(clientRepository.findById(1L).get(),
                                Map.of(1L, 2, 2L, 2),
                                new BigDecimal("5999.96")));
    }

    @Test
    void shouldPlaceOrderThrowProductNotFoundException() {
        Client client = new Client("Filip", "filip.ostrowicki@wp.pl");
        client.getCart().getProducts().put(1L, 2);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> orderService.placeOrder(1L))
                .extracting(ProductNotFoundException::getMessage)
                .isEqualTo("Nie znaleziono produktu o id " + 1L);
    }
}
