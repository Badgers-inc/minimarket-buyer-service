package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
