package service.impl;

import entity.Product;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import repository.ProductRepository;
import service.ProductManager;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
public class ProductManagerImpl implements ProductManager {
    private final ProductRepository productRepository;

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        productRepository.remove(product);
    }

    @Override
    public void updateProduct(long productId, BigDecimal newPrice, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        if (newPrice != null && newPrice.compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(newPrice);
        }

        if (newQuantity > 0) {
            product.setQuantity(newQuantity);
        }
    }

    @Override
    public Map<Long, Product> getAllProducts() {
        return productRepository.findAll();
    }
}
