package product.repository;

import product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    void remove(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByName(String name);

    List<Product> findByType(String type);
}
