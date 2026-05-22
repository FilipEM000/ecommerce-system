package service;

import entity.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ProductManager {
    void addProduct(Product product);

    void deleteProduct(long productId);

    void updateProduct(long productId, BigDecimal newPrice, int newQuantity);

    Map<Long, Product> getAllProducts();
}
