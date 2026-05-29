package service;

import dto.OrderDto;

public interface OrderService {
    OrderDto placeOrder(Long clientId);
}
