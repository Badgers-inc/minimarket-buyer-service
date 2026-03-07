package org.badgers.buyerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Offer;
import org.badgers.buyerservice.repository.OfferRepository;
import org.badgers.buyerservice.service.OfferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Override
    public Offer createOffer(Offer offer) {
        return null;
    }

    @Override
    public Offer getOfferById(UUID id) {
        return null;
    }

    @Override
    public void deleteOfferById(UUID id) {

    }

    @Override
    public List<Offer> getAllOffersBySellerId(UUID sellerId) {
        return List.of();
    }

    @Override
    public List<Offer> getAllOffersByProductId(UUID productId) {
        return List.of();
    }

    @Override
    public List<Offer> getAllOffers() {
        return List.of();
    }

    @Override
    public Offer updatePrice(UUID offerId, BigDecimal price) {
        return null;
    }

    @Override
    public Offer updateStock(UUID offerId, int stock) {
        return null;
    }
}
