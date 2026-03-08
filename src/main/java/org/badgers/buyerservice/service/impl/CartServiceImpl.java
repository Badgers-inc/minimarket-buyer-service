package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Buyer;
import org.badgers.buyerservice.entity.Cart;
import org.badgers.buyerservice.repository.CartRepository;
import org.badgers.buyerservice.service.CartService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public Cart create(Buyer buyer) {
        log.debug("creating cart for buyer id {}", buyer.getId());
        validateBuyer(buyer);
        if (cartRepository.existsCartByBuyer_Id(buyer.getId())) {
            throw new IllegalArgumentException("buyer already has cart");
        }
        Cart cart = new Cart();
        cart.setBuyer(buyer);
        log.debug("created cart for buyer id {}", buyer.getId());
        return cartRepository.save(cart);
    }

    @Override
    public void delete(Long id) {
        log.debug("deleting cart for buyer id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        try {
            cartRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("cart doesn't exist");
        }
        log.debug("deleted cart for buyer id {}", id);
    }

    @Override
    public Cart findById(Long id) {
        log.debug("getting cart for buyer id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("cart doesn't exist"));
        log.debug("found cart for buyer id {}", id);
        return cart;
    }

    @Override
    public List<Cart> findAll() {
        log.debug("getting all carts");
        List<Cart> carts = cartRepository.findAll();
        log.debug("found all carts");
        return carts;
    }

    @Override
    public Cart findByBuyerId(UUID buyerId) {
        log.debug("getting cart for buyer id {}", buyerId);
        if (buyerId == null) {
            throw new IllegalArgumentException("buyerId is null");
        }
        Cart cart = cartRepository.findByBuyerId(buyerId);
        if (cart == null) {
            throw new EntityNotFoundException("cart doesn't exist");
        }
        log.debug("found cart for buyer id {}", buyerId);
        return cart;
    }

    private void validateBuyer(Buyer buyer) {
        if (buyer.getId() == null) {
            throw new IllegalArgumentException("buyer isn't registered");
        }
        if (buyer.getFirstName() == null || buyer.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("buyer isn't registered");
        }
        if (buyer.getLastName() == null || buyer.getLastName().isEmpty()) {
            throw new IllegalArgumentException("buyer isn't registered");
        }
    }
}
