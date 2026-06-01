package product.repository;

import product.entity.Product;

import java.util.Map;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    void remove(Product product);

    Optional<Product> findById(Long id);

    Map<Long, Product> findAll();

    Long getNextId();
}
