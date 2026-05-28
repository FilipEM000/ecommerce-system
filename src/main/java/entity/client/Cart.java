package entity.client;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Cart {
    private Map<Long, Integer> products;

    public Cart(){
        this.products = new HashMap<>();
    }
}
