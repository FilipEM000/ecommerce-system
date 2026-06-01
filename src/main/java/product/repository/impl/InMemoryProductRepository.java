package product.repository.impl;

import product.entity.Product;
import product.repository.ProductRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryProductRepository implements ProductRepository {
    private Map<Long, Product> products = new HashMap<>();
    private Long productCounter = 0L;

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(getNextId());
        }
        products.put(product.getId(), product);
        return product;
    }

    public void remove(Product product) {
        products.remove(product.getId());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public Map<Long, Product> findAll() {
        return Collections.unmodifiableMap(products);
    }

    public Long getNextId(){
        return productCounter++;
    }
}
