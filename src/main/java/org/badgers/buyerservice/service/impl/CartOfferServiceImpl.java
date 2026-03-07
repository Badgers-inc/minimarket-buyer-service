package org.badgers.buyerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Cart;
import org.badgers.buyerservice.entity.CartOffer;
import org.badgers.buyerservice.entity.Offer;
import org.badgers.buyerservice.repository.CartOfferRepository;
import org.badgers.buyerservice.repository.CartRepository;
import org.badgers.buyerservice.repository.OfferRepository;
import org.badgers.buyerservice.service.CartOfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartOfferServiceImpl implements CartOfferService {

    private final CartOfferRepository cartOfferRepository;
    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;

    @Override
    @Transactional
    public CartOffer createCartOffer(CartOffer cartOffer) {
        log.debug("start create CartOffer");
        validateCartOffer(cartOffer);
        cartOffer.setActive(true);
        CartOffer saved = cartOfferRepository.save(cartOffer);
        log.debug("end create CartOffer");
        return saved;
    }

    @Override
    public CartOffer updateCartOffer(CartOffer cartOffer) {
        return null;
    }

    @Override
    public void deleteCartOffer(CartOffer cartOffer) {

    }

    @Override
    public CartOffer getCartOfferById(Long id) {
        return null;
    }

    @Override
    public CartOffer addOfferToCart(Long cartId, Long offerId, int quantity) {
        return null;
    }

    @Override
    public CartOffer getCartOfferByCartId(Long cartId) {
        return null;
    }

    private void validateCartOffer(CartOffer cartOffer) {
        if (cartOffer == null) {
            throw new NullPointerException("cartOffer is null");
        }
        if (cartOffer.getCart() == null || cartOffer.getCart().getId() == null) {
            throw new IllegalArgumentException("Cart with ID is required");
        }
        if (cartOffer.getOffer() == null || cartOffer.getOffer().getId() == null) {
            throw new IllegalArgumentException("Offer with ID is required");
        }
        if (cartOffer.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (cartOffer.getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }
        if (cartOffer.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        Cart cart = cartRepository.findById(cartOffer.getCart().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Cart with ID " + cartOffer.getCart().getId() + " not found"));
        Offer offer = offerRepository.findById(cartOffer.getOffer().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Offer with ID " + cartOffer.getOffer().getId() + " not found"));
        if (cartOfferRepository.existsByCartId(cartOffer.getCart().getId())) {
            throw new IllegalArgumentException("Cart with ID " + cartOffer.getCart().getId() + " already exists");
        }
    }
}
