package service;

import entity.Product;

import java.math.BigDecimal;
import java.util.Map;

public interface ProductService {
    void addProduct(Product product);

    void deleteProduct(Long productId);

    void updateProductPrice(Long productId, BigDecimal newPrice);

    void updateProductQuantity(Long productId, int newQuantity);

    Map<Long, Product> getAllProducts();
}
