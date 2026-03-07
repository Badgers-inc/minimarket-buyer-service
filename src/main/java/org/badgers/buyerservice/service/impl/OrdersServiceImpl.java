package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Orders;
import org.badgers.buyerservice.repository.OrdersRepository;
import org.badgers.buyerservice.service.OrdersService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public Orders createOrder(Orders orders) {
        log.debug("creating order");
        if (orders == null) {
            throw new IllegalArgumentException("Orders cannot be null");
        }
        Orders saved = ordersRepository.save(orders);
        log.debug("created order");
        return saved;
    }

    @Override
    public Orders getOrderById(Long orderId) {
        log.debug("getting order by id {}", orderId);
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + orderId));
        log.debug("finish getting order by id {}", orderId);
        return order;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        log.debug("deleting order by id {}", orderId);
        try {
            ordersRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Order not found with id " + orderId);
        }
        log.debug("order by id {} was deleted", orderId);
    }

    @Override
    @Transactional
    public Orders updateCompleted(Long orderId, Boolean completed) {
        log.debug("updating completed order by id {}", orderId);
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + orderId));
        order.setCompleted(completed);
        log.debug("updated completed order by id {}", orderId);
        return order;
    }

    @Override
    public List<Orders> getAllOrders() {
        log.debug("getting all orders");
        List<Orders> orders = ordersRepository.findAll();
        log.debug("all orders {}", orders);
        return orders;
    }
}
