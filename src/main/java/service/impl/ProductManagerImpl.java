package service.impl;

import entity.Product;
import exception.InvalidPriceException;
import exception.InvalidQuantityException;
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
    public void updateProductPrice(long productId, BigDecimal newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        if (newPrice == null) {
            throw new InvalidPriceException("Nie podałeś ceny");
        }

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Cena musi być większa od 0");
        }

        product.setPrice(newPrice);
    }

    @Override
    public void updateProductQuantity(long productId, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        if (newQuantity >= 0) {
            product.setQuantity(newQuantity);
        } else {
            throw new InvalidQuantityException("Dostępna ilość produktu nie może być mniejsza niż 0");
        }
    }

    @Override
    public Map<Long, Product> getAllProducts() {
        return productRepository.findAll();
    }
}
