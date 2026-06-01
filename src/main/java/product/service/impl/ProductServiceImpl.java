package product.service.impl;

import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import product.dto.ProductDto;
import product.entity.Product;
import product.mapper.ProductMapper;
import product.repository.ProductRepository;
import product.service.ProductService;

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

    @Override
    public List<ProductDto> getProductsByName(String name) {
        return productRepository.findAll().values().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .map(ProductMapper::mapToDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByType(String type) {
        return productRepository.findAll().values().stream()
                .filter(product -> product.getProductType().toLowerCase().contains(type.toLowerCase()))
                .map(ProductMapper::mapToDto)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        return ProductMapper.mapToDto(product);
    }

    @Override
    public void configureComputer(Long productId, String processorName, String ramName) {

    }

    @Override
    public void configureSmartphone(Long productId, String colorName, String batteryName) {

    }
}
