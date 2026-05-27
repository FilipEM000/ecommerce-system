package service;

import entity.Cart;

public interface CartService {
    void addProductToCart(Long clientId, Long productId, Integer quantity);

    Cart getAllProductsInCart(Long clientId);

    void checkout(Long clientId);
}
