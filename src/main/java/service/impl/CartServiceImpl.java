package service.impl;

import entity.Cart;
import entity.Client;
import entity.Product;
import exception.ClientNotFoundException;
import exception.NotEnoughQuantityInMagazineException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import repository.ClientRepository;
import repository.ProductRepository;
import service.CartService;

@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Override
    public void addProductToCart(Long clientId, Long productId, Integer quantity) {
        Client client = findClientOrThrow(clientId);

        Product product = findProductOrThrow(productId);

        if (product.getQuantity() >= client.getCart().getProducts().getOrDefault(productId, 0) + quantity) {
            client.getCart().getProducts().merge(productId, quantity, Integer::sum);
        } else {
            throw new NotEnoughQuantityInMagazineException("Nie ma wystarczająco produktu na stanie");
        }
    }

    @Override
    public Cart getAllProductsInCart(Long clientId) {
        Client client = findClientOrThrow(clientId);

        return client.getCart();
    }

    @Override
    public void checkout(Long clientId) {
        Client client = findClientOrThrow(clientId);

        processCart(client.getCart());

        client.getCart().getProducts().clear();
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

    private Client findClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }
}
