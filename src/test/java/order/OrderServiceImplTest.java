package order;

import order.dto.OrderDto;
import order.entity.Order;
import client.entity.Client;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import client.repository.impl.InMemoryClientRepository;
import order.repository.impl.InMemoryOrderRepository;
import product.repository.impl.InMemoryProductRepository;
import order.service.InvoiceGenerator;
import client.service.impl.CartServiceImpl;
import order.service.impl.OrderServiceImpl;

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
    InMemoryOrderRepository inMemoryOrderRepository;

    @Mock
    InMemoryClientRepository inMemoryClientRepository;

    @Mock
    InMemoryProductRepository inMemoryProductRepository;

    @Mock
    CartServiceImpl cartService;

    @Mock
    InvoiceGenerator invoiceGenerator;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void shouldPlaceOrder() {
        Client client = new Client("Filip", "filip.ostrowicki@wp.pl");
        Computer computer = new Computer("ASUS", new BigDecimal("999.99"), 2);
        computer.setId(1L);
        Smartphone smartphone = new Smartphone("iPhone", new BigDecimal("1999.99"), 2);
        smartphone.setId(2L);
        when(inMemoryClientRepository.findById(any())).thenReturn(Optional.of(client));
        client.getCart().getProducts().put(computer, 2);
        client.getCart().getProducts().put(smartphone, 2);
        when(inMemoryProductRepository.findById(1L))
                .thenReturn(Optional.of(computer));
        when(inMemoryProductRepository.findById(2L))
                .thenReturn(Optional.of(smartphone));
        when(inMemoryOrderRepository.save(any()))
                .thenReturn(new Order(client, Map.of(computer, 2, smartphone, 2), new BigDecimal("5999.96")));

        var result = orderService.placeOrder(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        new OrderDto(inMemoryClientRepository.findById(1L).get().getId(),
                                "Filip",
                                new BigDecimal("5999.96")));
    }

    @Test
    void shouldPlaceOrderThrowProductNotFoundException() {
        Client client = new Client("Filip", "filip.ostrowicki@wp.pl");
        Computer computer = new Computer("ASUS", new BigDecimal("999.99"), 2);
        computer.setId(1L);
        client.getCart().getProducts().put(computer, 2);

        when(inMemoryClientRepository.findById(any())).thenReturn(Optional.of(client));
        when(inMemoryProductRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> orderService.placeOrder(1L))
                .extracting(ProductNotFoundException::getMessage)
                .isEqualTo("Nie znaleziono produktu o id " + 1L);
    }

    @Test
    void shouldPlaceOrderThrowNotEnoughQuantityInMagazineException() {
        Client client = new Client("Filip", "filip.ostrowicki@wp.pl");
        Computer computer = new Computer("ASUS", new BigDecimal("999.99"), 2);
        computer.setId(1L);
        client.getCart().getProducts().put(computer, 3);

        when(inMemoryClientRepository.findById(any())).thenReturn(Optional.of(client));
        when(inMemoryProductRepository.findById(1L))
                .thenReturn(Optional.of(new Computer("ASUS", new BigDecimal("999.99"), 2)));

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> orderService.placeOrder(1L))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie ma wystarczająco produktu na stanie");
    }
}
