package product.repository.impl;

import product.entity.Product;
import product.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong productCounter = new AtomicLong(0);

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

    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    public List<Product> findByName(String name) {
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().startsWith(name.toLowerCase()))
                .toList();
    }

    public List<Product> findByType(String type) {
        return products.values().stream()
                .filter(product -> product.getProductType().name().equalsIgnoreCase(type))
                .toList();
    }

    private Long getNextId() {
        return productCounter.getAndIncrement();
    }
}
