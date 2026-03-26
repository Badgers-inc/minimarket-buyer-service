package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findOffersBySellerId(UUID sellerId);

    List<Offer> findOffersByProductId(UUID productId);
}
