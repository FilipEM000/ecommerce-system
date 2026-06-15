package order.repository.impl;

import order.entity.Order;
import order.repository.OrderRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong orderCounter = new AtomicLong(0);

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(getNextId());
        }
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

    public Long getNextId() {
        return orderCounter.getAndIncrement();
    }
}
