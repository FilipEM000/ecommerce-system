package order.entity;

import client.entity.Client;
import common.TimeConfig;
import exception.IDAlreadyExistException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import product.entity.Product;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Order {
    @EqualsAndHashCode.Include
    private Long id;
    private Client client;
    private Map<Product, Integer> products;
    private BigDecimal cost;
    private ZonedDateTime orderDate;

    public Order(Client client, Map<Product, Integer> products, BigDecimal cost) {
        this.client = client;
        this.products = new HashMap<>(products);
        this.cost = cost;
        this.orderDate = ZonedDateTime.now(TimeConfig.APP_ZONE);
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IDAlreadyExistException("ID zamówienia zostało już nadane");
        }
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}
