package order.service;

import client.dto.AddToCartRequest;
import client.entity.Client;
import client.repository.ClientRepository;
import client.repository.impl.InMemoryClientRepository;
import client.service.CartService;
import client.service.impl.CartServiceImpl;
import discount.PercentageDiscountPolicy;
import discount.ThresholdDiscountPolicy;
import discount.repository.impl.InMemoryDiscountRepository;
import discount.service.DiscountService;
import exception.EmptyCartException;
import exception.InvalidPromoCodeException;
import exception.NotEnoughQuantityInMagazineException;
import order.dto.OrderDto;
import order.repository.impl.InMemoryInvoiceRepository;
import order.repository.impl.InMemoryOrderRepository;
import order.service.impl.InvoiceGeneratorImpl;
import order.service.impl.OrderFileWriter;
import order.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.entity.Electronics;
import product.entity.computer.Computer;
import product.repository.ProductRepository;
import product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderServiceTestIT {
    private OrderServiceImpl orderService;
    private CartService cartService;
    private DiscountService discountService;
    private Client client;
    private Computer computer;
    private Electronics electronics;

    @BeforeEach
    void setUp() {
        ClientRepository clientRepository = new InMemoryClientRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        cartService = new CartServiceImpl(productRepository, clientRepository);
        discountService = new DiscountService(new InMemoryDiscountRepository());
        orderService = new OrderServiceImpl(
                new InMemoryOrderRepository(),
                clientRepository,
                productRepository,
                cartService,
                new InvoiceGeneratorImpl(new InMemoryInvoiceRepository()),
                new OrderFileWriter("test_orders.json"),
                discountService
        );

        client = clientRepository.save(new Client("Filip", "filip@wp.pl"));
        computer = (Computer) productRepository.save(new Computer("Dell", new BigDecimal("5000.00"), 10));
        electronics = (Electronics) productRepository.save(new Electronics("Mysz", new BigDecimal("200.00"), 20));
    }

    @Test
    void shouldPlaceOrderAndReturnCorrectCost() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 2));

        OrderDto order = orderService.placeOrder(client.getId(), null);

        assertThat(order.clientName()).isEqualTo("Filip");
        assertThat(order.cost()).isEqualByComparingTo("10000.00");
        assertThat(order.orderDate()).isNotNull();
    }

    @Test
    void shouldApplyPercentageDiscount() {
        discountService.addNewPromoCode("RABAT20", new PercentageDiscountPolicy(0.20));
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 1));

        OrderDto order = orderService.placeOrder(client.getId(), "RABAT20");

        // 5000 - 20% = 4000
        assertThat(order.cost()).isEqualByComparingTo("4000.00");
    }

    @Test
    void shouldApplyThresholdDiscount() {
        discountService.addNewPromoCode("DUZY500", new ThresholdDiscountPolicy(new BigDecimal("3000"), new BigDecimal("500")));
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 1));

        OrderDto order = orderService.placeOrder(client.getId(), "DUZY500");

        // 5000 - 500 = 4500
        assertThat(order.cost()).isEqualByComparingTo("4500.00");
    }

    @Test
    void shouldNotApplyThresholdDiscountWhenBelowThreshold() {
        discountService.addNewPromoCode("DUZY500", new ThresholdDiscountPolicy(new BigDecimal("10000"), new BigDecimal("500")));
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 1));

        OrderDto order = orderService.placeOrder(client.getId(), "DUZY500");

        assertThat(order.cost()).isEqualByComparingTo("5000.00");
    }

    @Test
    void shouldThrowExceptionWhenUsingInvalidPromoCode() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 1));

        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> orderService.placeOrder(client.getId(), "XXX"))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Kod rabatowy XXX nie istnieje.");
    }

    @Test
    void shouldReduceStockAfterOrder() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 3));

        orderService.placeOrder(client.getId(), null);

        assertThat(computer.getQuantity()).isEqualTo(7);
    }

    @Test
    void shouldClearCartAfterOrder() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), electronics.getId(), 5));

        orderService.placeOrder(client.getId(), null);

        assertThat(cartService.getAllProductsInCart(client.getId()).getProducts()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {
        assertThatExceptionOfType(EmptyCartException.class)
                .isThrownBy(() -> orderService.placeOrder(client.getId(), null))
                .extracting(EmptyCartException::getMessage)
                .isEqualTo("Nie można złożyć zamówienia, koszyk jest pusty");
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughStock() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), computer.getId(), 10));

        computer.setQuantity(3);

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> orderService.placeOrder(client.getId(), null));
    }
}
