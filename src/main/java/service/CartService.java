package service;

import entity.client.Cart;

public interface CartService {
    void addProductToCart(Long clientId, Long productId, Integer quantity);

    Cart getAllProductsInCart(Long clientId);

    void clearCart(Long clientId);
}
