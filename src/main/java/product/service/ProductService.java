package product.service;

import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import exception.ProductNotFoundException;
import product.dto.ProductDto;
import product.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    /**
     * Adds a new product to the repository.
     *
     * @param product The product entity to be added.
     * @return {@link ProductDto} containing the newly added product's details and ID.
     */
    ProductDto addProduct(Product product);

    /**
     * Removes a product from the system.
     *
     * @param productId The ID of the product to remove.
     * @throws ProductNotFoundException if the product does not exist.
     */
    void deleteProduct(Long productId);

    /**
     * Updates the price of an existing product.
     *
     * @param productId The ID of the product to update.
     * @param newPrice  The new price to set.
     * @throws ProductNotFoundException if the product does not exist.
     * @throws InvalidPriceException    if the price is null or less than/equal to zero.
     */
    void updateProductPrice(Long productId, BigDecimal newPrice);

    /**
     * Updates the available stock quantity of an existing product.
     *
     * @param productId   The ID of the product to update.
     * @param newQuantity The new quantity to set.
     * @throws ProductNotFoundException if the product does not exist.
     * @throws InvalidQuantityException if the quantity is negative.
     */
    void updateProductQuantity(Long productId, int newQuantity);

    /**
     * Retrieves all products currently available in the system.
     *
     * @return A list of {@link ProductDto} representing all products.
     */
    List<ProductDto> getAllProducts();

    /**
     * Searches for products by their name.
     *
     * @param name The partial or full name to search for (case-insensitive).
     * @return A list of {@link ProductDto} matching the given name.
     */
    List<ProductDto> getProductsByName(String name);

    /**
     * Searches for products by their type (e.g., Computer, Smartphone).
     *
     * @param type The type to search for (case-insensitive).
     * @return A list of {@link ProductDto} matching the given type.
     */
    List<ProductDto> getProductsByType(String type);

    /**
     * Retrieves a single product by its ID.
     *
     * @param productId The ID of the product.
     * @return {@link ProductDto} representing the product.
     * @throws ProductNotFoundException if the product does not exist.
     */
    ProductDto getProductById(Long productId);
}
