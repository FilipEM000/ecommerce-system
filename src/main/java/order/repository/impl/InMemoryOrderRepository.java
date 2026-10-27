package order.repository.impl;

import order.entity.Order;
import order.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
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
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> findAll() {
        return List.copyOf(orders.values());
    }

    private Long getNextId() {
        return orderCounter.getAndIncrement();
    }
}
