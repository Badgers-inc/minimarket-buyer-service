package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Buyer;
import org.badgers.buyerservice.entity.Cart;

import java.util.List;
import java.util.UUID;

public interface CartService {

    Cart create(Buyer buyer);

    void delete(Long id);

    Cart findById(Long id);

    List<Cart> findAll();

    Cart findByBuyerId(UUID buyerId);

}