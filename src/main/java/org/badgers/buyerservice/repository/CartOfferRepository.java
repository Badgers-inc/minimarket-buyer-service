package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.CartOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartOfferRepository extends JpaRepository<CartOffer, Long> {

    boolean existsByCartId(Long cartId);

    CartOffer findByCart_Id(Long cartId);
}
