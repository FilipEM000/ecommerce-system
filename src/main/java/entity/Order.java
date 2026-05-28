package entity;

import entity.client.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class Order {
    @Setter
    private Long id;
    private Client client;
    private Map<Long, Integer> products;
    private BigDecimal cost;

    public Order(Client client, Map<Long, Integer> products, BigDecimal cost) {
        this.client = client;
        this.products = products;
        this.cost = cost;
    }
}
