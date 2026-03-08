package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Offer;
import org.badgers.buyerservice.repository.OfferRepository;
import org.badgers.buyerservice.repository.ProductRepository;
import org.badgers.buyerservice.repository.SellerRepository;
import org.badgers.buyerservice.service.OfferService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private static final int MIN_VALUE_PRICE = 0;
    private static final int MIN_VALUE_UNITS = 0;

    @Override
    @Transactional
    public Offer createOffer(Offer offer) {
        log.debug("start creating offer");
        validateOffer(offer);
        if (!productRepository.existsById(offer.getProduct().getId())) {
            throw new EntityNotFoundException("product with id " + offer.getProduct().getId() + " not found");
        }
        if (!sellerRepository.existsById(offer.getSeller().getId())) {
            throw new EntityNotFoundException("seller with id " + offer.getSeller().getId() + " not found");
        }
        Offer createdOffer = offerRepository.save(offer);
        log.debug("end creating offer");
        return createdOffer;
    }

    @Override
    @Transactional(readOnly = true)
    public Offer getOfferById(UUID id) {
        log.debug("start getting offer by id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("offer with id " + id + " not found"));
        log.debug("end getting offer by id {}", id);
        return offer;
    }

    @Override
    public void deleteOfferById(UUID id) {
        log.debug("start deleting offer by id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        try {
            offerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("offer with id " + id + " not found");
        }
        log.debug("deleting offer by id {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Offer> getAllOffersBySellerId(UUID sellerId) {
        log.debug("start getting all offers by seller id {}", sellerId);
        if (sellerId == null) {
            throw new IllegalArgumentException("sellerId can't be null");
        }
        List<Offer> offers = offerRepository.findOffersBySeller_Id(sellerId);
        if (offers.isEmpty()) {
            log.debug("Offer's list is empty");
        }
        log.debug("end getting all offers by seller id {}", sellerId);
        return offers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Offer> getAllOffersByProductId(UUID productId) {
        log.debug("start getting all offers by product id {}", productId);
        if (productId == null) {
            throw new IllegalArgumentException("productId can't be null");
        }
        List<Offer> offers = offerRepository.findOffersByProduct_Id(productId);
        if (offers.isEmpty()) {
            log.debug("Offer's list is empty");
        }
        log.debug("end getting all offers by product id {}", productId);
        return offers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Offer> getAllOffers() {
        log.debug("start getting all offers");
        List<Offer> offers = offerRepository.findAll();
        if (offers.isEmpty()) {
            log.debug("Offers list is empty");
        }
        log.debug("end getting all offers");
        return offers;
    }

    @Override
    @Transactional
    public Offer updatePrice(UUID offerId, BigDecimal price) {
        log.debug("start updating offer price {}", offerId);
        if (price == null || price.compareTo(BigDecimal.ZERO) < MIN_VALUE_PRICE) {
            throw new IllegalArgumentException("price can't be null or negative");
        }
        if (offerId == null) {
            throw new IllegalArgumentException("offerId can't be null");
        }
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("offer with id " + offerId + " not found"));
        if (!offer.isActive()) {
            throw new IllegalArgumentException("offer isn't active");
        }
        offer.setPrice(price);
        log.debug("end updating offer price {}", offerId);
        return offer;
    }

    @Override
    @Transactional
    public Offer updateStock(UUID offerId, int stock) {
        log.debug("start updating offer stock {}", offerId);
        if (offerId == null) {
            throw new IllegalArgumentException("offerId can't be null");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("stock can't be negative");
        }
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("offer with id " + offerId + " not found"));
        offer.setUnitsInStock(stock);
        log.debug("end updating offer stock {}", offerId);
        return offer;
    }

    private void validateOffer(Offer offer) {
        if (offer == null) {
            throw new IllegalArgumentException("offer can't be null");
        }
        if (offer.getProduct() == null) {
            throw new IllegalArgumentException("product can't be null");
        }
        if (offer.getSeller() == null) {
            throw new IllegalArgumentException("seller can't be null");
        }
        if (offer.getPrice() == null) {
            throw new IllegalArgumentException("price can't be null");
        }
        if (offer.getUnitsInStock() < MIN_VALUE_UNITS) {
            throw new IllegalArgumentException("units in stock can't be negative");
        }
    }

}
