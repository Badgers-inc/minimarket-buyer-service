package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Seller;
import org.badgers.buyerservice.repository.SellerRepository;
import org.badgers.buyerservice.service.SellerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private static final int MAX_VALUE_LENGTH = 50;

    @Override
    @Transactional
    public Seller createSeller(Seller seller) {
        log.debug("starting create Seller");
        validateSeller(seller);
        String trimmedName = seller.getName().trim();
        seller.setName(trimmedName);
        if (existsSellerByName(trimmedName)) {
            throw new EntityExistsException("Seller with name " + trimmedName + " already exists");
        }
        Seller newSeller = sellerRepository.save(seller);
        log.debug("created Seller {}", newSeller);
        return newSeller;
    }

    @Override
    public void deleteSeller(UUID id) {
        log.debug("starting delete Seller");
        if (!sellerRepository.existsById(id)) {
            throw new EntityNotFoundException("Seller with id " + id + " not found");
        }
        sellerRepository.deleteById(id);
        log.debug("deleted Seller with id: {}", id);
    }

    @Override
    public Seller getSellerById(UUID id) {
        log.debug("starting getSellerById");
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller with ID " + id + " not found"));
        log.debug("getSellerById {}", seller);
        return seller;
    }

    @Override
    @Transactional
    public Seller updateSellerName(UUID id, String name) {
        log.debug("starting update Seller's name");
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller with ID " + id + " not found"));
        String trimmedName = trimmedString(name);
        if (existsSellerByName(trimmedName)) {
            throw new EntityExistsException("Seller with name " + trimmedName + " already exists");
        }
        seller.setName(trimmedName);
        log.debug("updated Seller's name {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getAllSellers() {
        log.debug("starting getAllSellers");
        List<Seller> sellers = sellerRepository.findAll();
        log.debug("getAllSellers {}", sellers);
        return sellers;
    }

    @Override
    public Seller getSellerByName(String name) {
        log.debug("starting getSellerByName");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Seller name is null");
        }
        Seller seller = sellerRepository.findSellerByName(trimmedString(name));
        if (seller == null) {
            throw new EntityNotFoundException("Seller with name " + name + " not found");
        }
        log.debug("getSellerByName {}", seller);
        return seller;
    }

    private void validateSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller is null");
        }
        if (seller.getName() == null || seller.getName().isEmpty() || seller.getName().length() > MAX_VALUE_LENGTH) {
            throw new IllegalArgumentException("Seller name is required");
        }
    }

    private String trimmedString(String string) {
        return string.trim();
    }

    private boolean existsSellerByName(String name) {
        return sellerRepository.existsByName(name);
    }
}