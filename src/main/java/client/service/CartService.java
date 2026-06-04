package client.service;

import client.dto.AddToCartRequest;
import client.entity.Cart;
import product.dto.ComputerConfiguration;
import product.dto.SmartphoneConfiguration;

public interface CartService {

    Cart getAllProductsInCart(Long clientId);

    void clearCart(Long clientId);

    void addStandardProductToCart(AddToCartRequest request);

    void addComputerToCart(AddToCartRequest request, ComputerConfiguration configuration);

    void addSmartphoneToCart(AddToCartRequest request, SmartphoneConfiguration configuration);
}
