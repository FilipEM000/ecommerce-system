package service.impl;

import dto.ProductDto;
import entity.Product;
import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import mapper.ProductMapper;
import repository.ProductRepository;
import service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDto addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToDto(savedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        productRepository.remove(product);
    }

    @Override
    public void updateProductPrice(Long productId, BigDecimal newPrice) {
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
    public void updateProductQuantity(Long productId, int newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        if (newQuantity >= 0) {
            product.setQuantity(newQuantity);
        } else {
            throw new InvalidQuantityException("Dostępna ilość produktu nie może być mniejsza niż 0");
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().values().stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }
}
