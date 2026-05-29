package repository;

import entity.Order;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepository {
    private Map<Long, Order> orders = new HashMap<>();
    private Long orderCounter = 0L;

    public Order save(Order order) {
        order.setId(getNextId());
        orders.putIfAbsent(order.getId(), order);
        return order;
    }

    public void remove(Order order) {
        orders.remove(order.getId());
    }

    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public Map<Long, Order> findAll() {
        return Collections.unmodifiableMap(orders);
    }

    public Long getNextId(){
        return orderCounter++;
    }
}
