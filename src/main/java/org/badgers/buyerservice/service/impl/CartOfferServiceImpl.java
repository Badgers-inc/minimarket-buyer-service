package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Cart;
import org.badgers.buyerservice.entity.CartOffer;
import org.badgers.buyerservice.entity.Offer;
import org.badgers.buyerservice.repository.CartOfferRepository;
import org.badgers.buyerservice.repository.CartRepository;
import org.badgers.buyerservice.repository.OfferRepository;
import org.badgers.buyerservice.service.CartOfferService;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static final int MIN_VALUE_PRICE = 0;
    private static final int MIN_VALUE_QUANTITY = 0;

    @Override
    @Transactional
    public CartOffer createCartOffer(CartOffer cartOffer) {
        log.debug("start creating CartOffer");
        validateCartOfferForCreate(cartOffer);
        CartOffer saved = cartOfferRepository.save(cartOffer);
        log.debug("end create CartOffer");
        return saved;
    }

    @Override
    @Transactional
    public CartOffer updateCartOffer(Long cartOfferId, CartOffer cartOffer) {
        log.debug("start updating CartOffer");
        if (cartOfferId == null) {
            throw new IllegalArgumentException("cartOfferId is null");
        }
        validateCartOfferForUpdate(cartOffer);
        CartOffer existCartOffer = cartOfferRepository.findById(cartOfferId)
                .orElseThrow(EntityNotFoundException::new);
        existCartOffer.setCart(cartOffer.getCart());
        existCartOffer.setOffer(cartOffer.getOffer());
        existCartOffer.setQuantity(cartOffer.getQuantity());
        existCartOffer.setPrice(cartOffer.getPrice());
        existCartOffer.setOrder(cartOffer.getOrder());
        log.debug("end updating CartOffer");
        return existCartOffer;
    }

    @Override
    public void deleteCartOffer(Long cartOfferId) {
        log.debug("start deleting CartOffer");
        if (cartOfferId == null) {
            throw new IllegalArgumentException("cartOfferId is null");
        }
        try {
            cartOfferRepository.deleteById(cartOfferId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("cartOffer doesn't exist");
        }
        log.debug("delete CartOffer");
    }

    @Override
    @Transactional(readOnly = true)
    public CartOffer getCartOfferById(Long id) {
        log.debug("start getting CartOffer");
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        CartOffer cartOffer = cartOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("cartOfferId is " + id));
        log.debug("get CartOffer");
        return cartOffer;
    }

    @Override
    @Transactional(readOnly = true)
    public CartOffer getCartOfferByCartId(Long cartId) {
        log.debug("start getting CartOffer");
        if (cartId == null) {
            throw new IllegalArgumentException("cartId is null");
        }
        CartOffer cartOffer = cartOfferRepository.findByCart_Id(cartId);
        if (cartOffer == null) {
            throw new EntityNotFoundException("cartOffer does not exist");
        }
        log.debug("get CartOffer");
        return cartOffer;
    }

    private void validateCartOfferForCreate(CartOffer cartOffer) {
        if (cartOffer == null) {
            throw new IllegalArgumentException("cartOffer is null");
        }
        Cart cart = cartRepository.findById(cartOffer.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart with ID " + cartOffer.getCart().getId()));
        Offer offer = offerRepository.findById(cartOffer.getOffer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Offer with ID " + cartOffer.getOffer().getId() + " does not exist"));
        if (cartOfferRepository.existsByCartId(cart.getId())) {
            throw new IllegalArgumentException("Cart with ID " + cart.getId() + " already exists");
        }
        if (cartOffer.getQuantity() < MIN_VALUE_QUANTITY) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (cartOffer.getPrice() == null || cartOffer.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price is required or can't be negative");
        }
        if (!cart.isActive()) {
            throw new IllegalArgumentException("Cart must be active");
        }
        if (!offer.isActive()) {
            throw new IllegalArgumentException("Offer is required or must be active");
        }
        if (offer.getPrice().compareTo(cartOffer.getPrice()) != MIN_VALUE_PRICE) {
            throw new IllegalArgumentException("Price isn't valid with offer");
        }
    }

    private void validateCartOfferForUpdate(CartOffer cartOffer) {
        log.debug("Validating CartOffer for update: {}", cartOffer);
        if (cartOffer == null) {
            throw new IllegalArgumentException("CartOffer cannot be null");
        }
        if (cartOffer.getQuantity() < MIN_VALUE_QUANTITY) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (cartOffer.getPrice() == null || cartOffer.getPrice().compareTo(BigDecimal.ZERO) < MIN_VALUE_PRICE) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (cartOffer.getCart() == null || cartOffer.getCart().getId() == null) {
            throw new IllegalArgumentException("Cart reference is invalid");
        }
        if (cartOffer.getOffer() == null || cartOffer.getOffer().getId() == null) {
            throw new IllegalArgumentException("Offer reference is invalid");
        }
    }
}