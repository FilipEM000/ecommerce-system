package repository;

import entity.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ProductRepository {
    private Map<Long, Product> products = new HashMap<>();

    public void save(Product product) {
        products.putIfAbsent(product.getId(), product);
    }

    public void remove(Product product) {
        products.remove(product.getId());
    }

    public Optional<Product> findById(long id) {
        return Optional.ofNullable(products.get(id));
    }

    public Map<Long, Product> findAll() {
        return Collections.unmodifiableMap(products);
    }
}
