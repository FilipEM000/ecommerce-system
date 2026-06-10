package product.service.impl;

import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import product.dto.ProductDto;
import product.entity.Product;
import product.mapper.ProductMapper;
import product.repository.ProductRepository;
import product.service.ProductService;
import product.validator.ProductValidator;

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
        ProductValidator.validatePrice(newPrice);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        product.setPrice(newPrice);
    }

    @Override
    public void updateProductQuantity(Long productId, int newQuantity) {
        ProductValidator.validateQuantity(newQuantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));

        product.setQuantity(newQuantity);
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
}
