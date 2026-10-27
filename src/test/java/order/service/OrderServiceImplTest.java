package order.service;

import client.entity.Client;
import client.repository.ClientRepository;
import client.service.impl.CartServiceImpl;
import discount.service.DiscountService;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import order.dto.OrderDto;
import order.entity.Invoice;
import order.entity.Order;
import order.repository.OrderRepository;
import order.service.impl.OrderFileWriter;
import order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.entity.computer.Computer;
import product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Mock
    InvoiceGenerator invoiceGenerator;

    @Mock
    OrderFileWriter orderFileWriter;

    @Mock
    DiscountService discountService;

    @Mock
    ExecutorService executorService;

    @InjectMocks
    OrderServiceImpl orderService;

    private Client client;
    private Computer computer;

    @BeforeEach
    void setUp() {
        client = new Client("Filip", "filip@wp.pl");
        computer = new Computer("ASUS", new BigDecimal("999.99"), 5);
        computer.setId(1L);

        when(discountService.getPolicyForCode(any()))
                .thenReturn(totalCost -> BigDecimal.ZERO);
        when(cartService.getCartTotalPrice(any()))
                .thenReturn(new BigDecimal("999.99"));
    }

    @Test
    void shouldPlaceOrderAndReturnDto() {
        client.getCart().getProducts().put(computer, 2);
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(client));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(computer));
        when(orderRepository.save(any()))
                .thenReturn(new Order(client, client.getCart().getProducts(), new BigDecimal("1999.98")));

        OrderDto orderDto = orderService.placeOrder(1L, null);

        assertThat(orderDto.clientName()).isEqualTo("Filip");
        assertThat(orderDto.cost()).isEqualTo(new BigDecimal("1999.98"));
        assertThat(orderDto.invoiceNumber()).isEqualTo("W TRAKCIE GENEROWANIA");
    }

    @Test
    void shouldCLearCartAfterPlacingOrder() {
        client.getCart().getProducts().put(computer, 1);
        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(client));
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(computer));
        when(orderRepository.save(any()))
                .thenReturn(new Order(client, client.getCart().getProducts(), new BigDecimal("999.99")));

        orderService.placeOrder(1L, null);

        verify(cartService, times(1)).clearCart(1L);
    }

    @Test
    void shouldPlaceOrderThrowProductNotFoundException() {
        client.getCart().getProducts().put(computer, 2);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> orderService.placeOrder(1L, null))
                .extracting(ProductNotFoundException::getMessage)
                .isEqualTo("Nie znaleziono produktu o id " + 1L);
    }

    @Test
    void shouldPlaceOrderThrowNotEnoughQuantityInMagazineException() {
        client.getCart().getProducts().put(computer, 10);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(computer));

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> orderService.placeOrder(1L, null))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Brakuje produktu na stanie: ASUS (wymagane: 10, dostępne: 5)");
    }
}