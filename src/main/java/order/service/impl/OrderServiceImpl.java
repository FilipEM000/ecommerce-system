package order.service.impl;

import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.CartService;
import exception.ClientNotFoundException;
import exception.EmptyCartException;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import order.dto.OrderDto;
import order.entity.Invoice;
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

        if (cart.getProducts().isEmpty()) {
            throw new EmptyCartException("Nie można złożyć zamówienia, koszyk jest pusty");
        }

        BigDecimal cartCost = getTotalCartCost(cart);
        Map<Product, Integer> products = new HashMap<>(cart.getProducts());

        processCart(cart);
        cartService.clearCart(clientId);

        Order order = new Order(client, products, cartCost);
        Order savedOrder = orderRepository.save(order);
        Invoice invoice = invoiceGenerator.generateInvoice(savedOrder);

        return OrderMapper.mapToDto(savedOrder, invoice.invoiceNumber());
    }

    private void processCart(Cart cart) {
        cart.getProducts().forEach((configuredProduct, quantityInCart) -> {
            Product masterProduct = findProductOrThrow(configuredProduct.getId());

            if (masterProduct.getQuantity() < quantityInCart) {
                throw new NotEnoughQuantityInMagazineException("Nie ma wystarczająco produktu na stanie");
            }

            masterProduct.setQuantity(masterProduct.getQuantity() - quantityInCart);
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
}
