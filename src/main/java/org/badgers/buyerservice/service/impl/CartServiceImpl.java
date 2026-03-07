package org.badgers.buyerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Cart;
import org.badgers.buyerservice.repository.CartRepository;
import org.badgers.buyerservice.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart save(Cart cart) {
        return null;
    }

    @Override
    public void delete(Cart cart) {

    }

    @Override
    public Cart findById(Long id) {
        return null;
    }

    @Override
    public List<Cart> findAll() {
        return List.of();
    }

    @Override
    public List<Cart> findByBuyerId(UUID buyerId) {
        return List.of();
    }
}
