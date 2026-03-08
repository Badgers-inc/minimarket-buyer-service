package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.CartOffer;

import java.util.UUID;

public interface CartOfferService {

    CartOffer createCartOffer(CartOffer cartOffer);

    CartOffer updateCartOffer(Long id, CartOffer cartOffer);

    void deleteCartOffer(Long cartOfferId);

    CartOffer getCartOfferById(Long id);

    CartOffer getCartOfferByCartId(Long cartId);


}
