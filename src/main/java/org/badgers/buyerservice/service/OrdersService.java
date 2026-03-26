package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Orders;

import java.util.List;

public interface OrdersService {

    Orders createOrder(Orders orders);

    Orders getOrderById(Long orderId);

    void deleteOrderById(Long orderId);

    Orders updateCompleted(Long orderId, Boolean completed);

    List<Orders> getAllOrders();

}
