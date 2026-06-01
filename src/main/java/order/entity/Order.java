package order.entity;

import client.entity.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Order {
    @Setter
    private Long id;
    private Client client;
    private Map<Product, Integer> products;
    private BigDecimal cost;
    private LocalDateTime orderDate;

    public Order(Client client, Map<Product, Integer> products, BigDecimal cost) {
        this.client = client;
        this.products = products;
        this.cost = cost;
        this.orderDate = LocalDateTime.now();
    }
}
