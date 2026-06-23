package client.entity;

import lombok.Getter;
import lombok.ToString;
import product.entity.Product;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public final class Cart {
    private final Map<Product, Integer> products;

    private Cart() {
        this.products = new HashMap<>();
    }

    public static Cart initialize() {
        return new Cart();
    }

    public void addProduct(Product product, int quantity) {
        products.merge(product, quantity, Integer::sum);
    }
}
