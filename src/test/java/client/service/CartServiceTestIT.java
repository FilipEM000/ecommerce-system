package client.service;

import client.dto.AddToCartRequest;
import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.repository.impl.InMemoryClientRepository;
import client.service.impl.CartServiceImpl;
import exception.InvalidProductTypeException;
import exception.InvalidQuantityException;
import exception.NotEnoughQuantityInMagazineException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.dto.ComputerConfiguration;
import product.entity.Electronics;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;
import product.repository.ProductRepository;
import product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CartServiceTestIT {
    private CartService cartService;
    private ProductRepository productRepository;
    private ClientRepository clientRepository;

    private Client client;
    private Computer computer;
    private Smartphone smartphone;
    private Electronics electronics;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        clientRepository = new InMemoryClientRepository();
        cartService = new CartServiceImpl(productRepository, clientRepository);

        client = clientRepository.save(new Client("Filip", "filip@wp.pl"));
        computer = (Computer) productRepository.save(new Computer("Dell XPS", new BigDecimal("5000"), 10));
        smartphone = (Smartphone) productRepository.save(new Smartphone("iPhone 15", new BigDecimal("4000"), 5));
        electronics = (Electronics) productRepository.save(new Electronics("Mysz Logitech", new BigDecimal("200"), 20));
    }

    @Test
    void shouldAddStandardProductToCart() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), electronics.getId(), 3));

        Cart cart = cartService.getAllProductsInCart(client.getId());

        assertThat(cart.getProducts()).hasSize(1);
    }

    @Test
    void shouldAddComputerWithConfiguration() {
        ComputerConfiguration config = new ComputerConfiguration("INTEL_CORE_I7", "DDR4_16_3200");

        cartService.addComputerToCart(new AddToCartRequest(client.getId(), computer.getId(), 1), config);

        Cart cart = cartService.getAllProductsInCart(client.getId());
        assertThat(cart.getProducts()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenAddingComputerConfigToSmartphone() {
        AddToCartRequest request = new AddToCartRequest(client.getId(), smartphone.getId(), 1);
        ComputerConfiguration config = new ComputerConfiguration("I7", "GB_16");

        assertThatExceptionOfType(InvalidProductTypeException.class)
                .isThrownBy(() -> cartService.addComputerToCart(request, config))
                .extracting(InvalidProductTypeException::getMessage)
                .isEqualTo("Produkt nie jest komputerem");
    }

    @Test
    void shouldThrowExceptionWhenAddingZeroQuantity() {
        AddToCartRequest request = new AddToCartRequest(client.getId(), electronics.getId(), 0);

        assertThatExceptionOfType(InvalidQuantityException.class)
                .isThrownBy(() -> cartService.addStandardProductToCart(request))
                .extracting(InvalidQuantityException::getMessage)
                .isEqualTo("Ilość dodawana do koszyka musi być większa niż 0");
    }

    @Test
    void shouldThrowWhenExceedingStock() {
        AddToCartRequest request = new AddToCartRequest(client.getId(), smartphone.getId(), 6);

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> cartService.addStandardProductToCart(request))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie masz wystarczającej ilości produktu na stanie. " +
                        "Dostępnych w magazynie: 5, w koszyku masz już: 0, próbujesz dodać: 6");
    }

    @Test
    void shouldMergeQuantitiesWhenAddingSameProductTwice() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), electronics.getId(), 2));
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), electronics.getId(), 3));

        Cart cart = cartService.getAllProductsInCart(client.getId());
        int totalQuantity = cart.getProducts().values().stream().mapToInt(Integer::intValue).sum();

        assertThat(totalQuantity).isEqualTo(5);
    }

    @Test
    void shouldClearCart() {
        cartService.addStandardProductToCart(new AddToCartRequest(client.getId(), electronics.getId(), 2));

        cartService.clearCart(client.getId());

        assertThat(cartService.getAllProductsInCart(client.getId()).getProducts()).isEmpty();
    }
}
