package client.entity;

import product.entity.Product;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class Cart {
    private Map<Product, Integer> products;

    public Cart(){
        this.products = new HashMap<>();
    }
}
