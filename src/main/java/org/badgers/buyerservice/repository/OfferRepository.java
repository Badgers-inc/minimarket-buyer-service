package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findOffersBySeller_Id(UUID sellerId);

    List<Offer> findOffersByProduct_Id(UUID productId);
}
