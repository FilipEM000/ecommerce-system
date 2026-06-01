package order.service;

import order.dto.OrderDto;

public interface OrderService {
    OrderDto placeOrder(Long clientId);
}
