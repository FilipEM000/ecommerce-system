package service;

import entity.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ProductManager {
    void addProduct(Product product);

    void deleteProduct(long productId);

    void updateProductPrice(long productId, BigDecimal newPrice);

    void updateProductQuantity(long productId, int newQuantity);

    Map<Long, Product> getAllProducts();
}
