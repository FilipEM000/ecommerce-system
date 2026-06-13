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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final InvoiceGenerator invoiceGenerator;
    private final OrderFileWriter orderFileWriter;
    private final DiscountService discountService;
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(4);


    @Override
    public OrderDto placeOrder(Long clientId, String promoCode) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
        Cart cart = client.getCart();

        if (cart.getProducts().isEmpty()) {
            throw new EmptyCartException("Nie można złożyć zamówienia, koszyk jest pusty");
        }

        BigDecimal cartCost = getTotalCartCost(cart);

        DiscountPolicy policy = discountService.getPolicyForCode(promoCode);
        BigDecimal discount = policy.calculateDiscount(cartCost);
        BigDecimal finalCost = cartCost.subtract(discount);

        Map<Product, Integer> products = new HashMap<>(cart.getProducts());
        processCart(cart);
        cartService.clearCart(clientId);

        Order order = new Order(client, products, finalCost);
        Order savedOrder = orderRepository.save(order);

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
        cart.getProducts().forEach((configuredProduct, quantityInCart) -> {
            Product masterProduct = findProductOrThrow(configuredProduct.getId());

            synchronized (masterProduct) {
                if (masterProduct.getQuantity() < quantityInCart) {
                    throw new NotEnoughQuantityInMagazineException("Nie ma wystarczająco produktu na stanie");
                }

                masterProduct.setQuantity(masterProduct.getQuantity() - quantityInCart);
            }
        });
    }

    private BigDecimal getTotalCartCost(Cart cart) {
        return cart.getProducts().entrySet().stream()
                .map(entry -> {
                    Product configuredProduct = entry.getKey();
                    return configuredProduct.getTotalPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product findProductOrThrow(Long productId) {
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
