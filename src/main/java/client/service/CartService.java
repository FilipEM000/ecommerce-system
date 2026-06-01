package client.service;

import client.entity.Cart;

public interface CartService {

    Cart getAllProductsInCart(Long clientId);

    void clearCart(Long clientId);

    void addStandardProductToCart(Long clientId, Long productId, Integer quantity);

    void addComputerToCart(Long clientId, Long productId, Integer quantity, String processor, String ram);

    void addSmartphoneToCart(Long clientId, Long productId, Integer quantity, String color, String battery);
}
