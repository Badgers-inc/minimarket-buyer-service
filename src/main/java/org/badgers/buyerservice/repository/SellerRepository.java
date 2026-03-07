package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Seller findSellerByName(String name);

    boolean existsByName(String name);
}