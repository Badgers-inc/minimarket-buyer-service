package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByBuyerId(UUID buyerId);

    boolean existsByBuyerId(UUID buyerId);
}
