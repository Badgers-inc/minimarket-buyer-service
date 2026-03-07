package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.CartOffer;

public interface CartOfferService {

    CartOffer createCartOffer(CartOffer cartOffer);

    CartOffer updateCartOffer(CartOffer cartOffer);

    void deleteCartOffer(CartOffer cartOffer);

    CartOffer getCartOfferById(Long id);

    CartOffer addOfferToCart(Long cartId, Long offerId, int quantity);

    CartOffer getCartOfferByCartId(Long cartId);


}
