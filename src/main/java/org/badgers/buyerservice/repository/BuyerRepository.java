package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID>, JpaSpecificationExecutor<Buyer> {

}
