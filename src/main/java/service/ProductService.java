package service;

import dto.ProductDto;
import entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDto addProduct(Product product);

    void deleteProduct(Long productId);

    void updateProductPrice(Long productId, BigDecimal newPrice);

    void updateProductQuantity(Long productId, int newQuantity);

    List<ProductDto> getAllProducts();
}
