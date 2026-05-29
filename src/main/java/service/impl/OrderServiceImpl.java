package service.impl;

import dto.OrderDto;
import entity.Order;
import entity.Product;
import entity.client.Cart;
import entity.client.Client;
import exception.ClientNotFoundException;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import mapper.OrderMapper;
import repository.ClientRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import service.CartService;
import service.InvoiceGenerator;
import service.OrderService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final InvoiceGenerator invoiceGenerator;

    @Override
    public OrderDto placeOrder(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
        Cart cart = client.getCart();

        BigDecimal cartCost = getTotalCartCost(cart);
        Map<Long, Integer> products = new HashMap<>(cart.getProducts());

        processCart(cart);
        cartService.clearCart(clientId);

        Order order = new Order(client, products, cartCost);
        Order savedOrder = orderRepository.save(order);
        invoiceGenerator.generateInvoice(savedOrder);
        return OrderMapper.mapToDto(savedOrder);
    }

    private void processCart(Cart cart) {
        cart.getProducts().forEach((productId, quantityInCart) -> {
            Product product = findProductOrThrow(productId);

            if (product.getQuantity() < quantityInCart) {
                throw new NotEnoughQuantityInMagazineException("Nie ma wystarczająco produktu na stanie");
            }

            product.setQuantity(product.getQuantity() - quantityInCart);
        });
    }

    private BigDecimal getTotalCartCost(Cart cart) {
        return cart.getProducts().entrySet().stream()
                .map(entry -> {
                    Product product = findProductOrThrow(entry.getKey());
                    return product.getTotalPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }
}
