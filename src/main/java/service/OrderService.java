package service;

import entity.Order;

public interface OrderService {
    Order placeOrder(Long clientId);
}
