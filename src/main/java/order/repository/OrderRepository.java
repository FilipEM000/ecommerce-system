package order.repository;

import order.entity.Order;

import java.util.Map;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    void remove(Order order);

    Optional<Order> findById(Long orderId);

    Map<Long, Order> findAll();

    Long getNextId();
}
