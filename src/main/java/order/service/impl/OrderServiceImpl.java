package order.service.impl;

import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.CartService;
import discount.DiscountPolicy;
import discount.service.DiscountService;
import exception.ClientNotFoundException;
import exception.EmptyCartException;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import order.dto.OrderDto;
import order.entity.Order;
import order.mapper.OrderMapper;
import order.repository.OrderRepository;
import order.service.InvoiceGenerator;
import order.service.OrderService;
import product.entity.Product;
import product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderFileWriter orderFileWriter;
    private final DiscountService discountService;
    private final ExecutorService asyncExecutor;

    @Override
    public OrderDto placeOrder(Long clientId, String promoCode) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));

        Cart cart = Optional.ofNullable(client.getCart())
                .filter(c -> !c.getProducts().isEmpty())
                .orElseThrow(() -> new EmptyCartException("Nie można złożyć zamówienia, koszyk jest pusty"));

        BigDecimal cartCost = cartService.getCartTotalPrice(clientId);

        DiscountPolicy policy = discountService.getPolicyForCode(promoCode);
        BigDecimal discount = policy.calculateDiscount(cartCost);
        BigDecimal finalCost = cartCost.subtract(discount);

        processCart(cart);

        Order order = new Order(client, cart.getProducts(), finalCost);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(clientId);

        CompletableFuture.runAsync(() -> {
            orderFileWriter.write(savedOrder);
            invoiceGenerator.generateInvoice(savedOrder);
        }, asyncExecutor).exceptionally(ex -> {
            System.err.println("Błąd podczas przetwarzania zamówienia: " + ex.getMessage());
            return null;
        });

        return OrderMapper.mapToDto(savedOrder, "W TRAKCIE GENEROWANIA");
    }

    private void processCart(Cart cart) {
        Map<Product, Integer> stockToDecrement = new HashMap<>();

        cart.getProducts().forEach((configuredProduct, quantityInCart) -> {
            Product masterProduct = findProduct(configuredProduct.getId());

            synchronized (masterProduct) {
                if (masterProduct.getQuantity() < quantityInCart) {
                    throw new NotEnoughQuantityInMagazineException(String.format("Brakuje produktu na stanie: %s (wymagane: %d, dostępne: %d)",
                            masterProduct.getName(), quantityInCart, masterProduct.getQuantity()));
                }
            }
            stockToDecrement.put(masterProduct, quantityInCart);
        });

        stockToDecrement.forEach((masterProduct, quantityInCart) -> {
            synchronized (masterProduct) {
                masterProduct.setQuantity(masterProduct.getQuantity() - quantityInCart);
            }
        });
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }

    public void shutdown() {
        asyncExecutor.shutdown();
        try {
            if (!asyncExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Wymuszono zamknięcie puli wątków — nie wszystkie zadania zostały ukończone.");
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
