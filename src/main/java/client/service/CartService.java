package client.service;

import client.dto.AddToCartRequest;
import client.entity.Cart;
import exception.InvalidProductTypeException;
import exception.InvalidQuantityException;
import exception.NotEnoughQuantityInMagazineException;
import product.dto.ComputerConfiguration;
import product.dto.SmartphoneConfiguration;

import java.math.BigDecimal;

public interface CartService {
    /**
     * Retrieves the entire cart for a specific client.
     *
     * @param clientId The ID of the client.
     * @return {@link Cart} containing the products and their quantities.
     */
    Cart getAllProductsInCart(Long clientId);

    /**
     * Empties the cart for a specific client.
     *
     * @param clientId The ID of the client whose cart should be cleared.
     */
    void clearCart(Long clientId);

    /**
     * Adds a standard (non-configurable) product to the client's cart.
     *
     * @param request DTO containing client ID, product ID, and desired quantity.
     * @throws InvalidQuantityException             if the quantity is zero or negative.
     * @throws NotEnoughQuantityInMagazineException if there is insufficient stock.
     */
    void addStandardProductToCart(AddToCartRequest request);

    /**
     * Configures a computer and adds it to the client's cart.
     *
     * @param request       DTO containing client ID, product ID, and desired quantity.
     * @param configuration DTO containing the selected Processor and RAM.
     * @throws InvalidProductTypeException          if the targeted product is not a Computer.
     * @throws InvalidQuantityException             if the quantity is zero or negative.
     * @throws NotEnoughQuantityInMagazineException if there is insufficient stock.
     */
    void addComputerToCart(AddToCartRequest request, ComputerConfiguration configuration);

    /**
     * Configures a smartphone and adds it to the client's cart.
     *
     * @param request       DTO containing client ID, product ID, and desired quantity.
     * @param configuration DTO containing the selected Color and Battery capacity.
     * @throws InvalidProductTypeException          if the targeted product is not a Smartphone.
     * @throws InvalidQuantityException             if the quantity is zero or negative.
     * @throws NotEnoughQuantityInMagazineException if there is insufficient stock.
     */
    void addSmartphoneToCart(AddToCartRequest request, SmartphoneConfiguration configuration);

    /**
     * Calculates the total cost of all products currently in the client's cart.
     *
     * @param clientId The ID of the client.
     * @return The total cost as {@link java.math.BigDecimal}.
     */
    BigDecimal getCartTotalPrice(Long clientId);
}
