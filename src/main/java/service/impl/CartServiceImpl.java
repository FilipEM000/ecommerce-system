package service.impl;

import entity.client.Cart;
import entity.client.Client;
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
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

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
        return findClientOrThrow(clientId).getCart();
    }

    @Override
    public void clearCart(Long clientId) {
        Cart cart = getAllProductsInCart(clientId);
        cart.getProducts().clear();
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
