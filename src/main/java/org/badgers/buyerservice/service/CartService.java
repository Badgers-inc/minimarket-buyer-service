package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {

    Cart save(Cart cart);

    void delete(Cart cart);

    Cart findById(Long id);

    List<Cart> findAll();

    List<Cart> findByBuyerId(UUID buyerId);

}