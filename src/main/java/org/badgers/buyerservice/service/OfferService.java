package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OfferService {

    Offer createOffer(Offer offer);

    Offer getOfferById(UUID id);

    void deleteOfferById(UUID id);

    List<Offer> getAllOffersBySellerId(UUID sellerId);

    List<Offer> getAllOffersByProductId(UUID productId);

    List<Offer> getAllOffers();

    Offer updatePrice(UUID offerId, BigDecimal price);

    Offer updateStock(UUID offerId, int stock);


}
