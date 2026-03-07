package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
