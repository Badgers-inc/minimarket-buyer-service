package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Buyer;
import org.badgers.buyerservice.repository.BuyerRepository;
import org.badgers.buyerservice.service.BuyerService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;

    @Override
    @Transactional
    public Buyer save(Buyer buyer) {
        log.debug("start save buyer : {}", buyer);
        if (buyer == null) {
            throw new IllegalArgumentException("buyer cannot be null");
        }
        if (buyer.getFirstName() == null || buyer.getLastName() == null) {
            throw new IllegalArgumentException("not valid buyer fields");
        }
        Buyer savedBuyer = buyerRepository.save(buyer);
        log.debug("end save buyer : {}", savedBuyer);
        return savedBuyer;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.debug("start delete buyer : {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        try {
            buyerRepository.deleteById(id);
            log.debug("end delete buyer : {}", id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("buyer with id " + id + " not found", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Buyer findById(UUID id) {
        log.debug("start find buyer by id : {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("buyer with id " + id + " not found"));
        log.debug("end find buyer by id : {}", buyer);
        return buyer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Buyer> findAll() {
        log.debug("start find all buyers");
        List<Buyer> buyers = buyerRepository.findAll();
        log.debug("end find all buyers");
        return buyers;
    }

    @Override
    @Transactional
    public Buyer updateById(UUID uuid, Buyer buyer) {
        log.debug("start full update buyer by id : {}", buyer);
        if (buyer == null || uuid == null) {
            throw new IllegalArgumentException("buyer or uuid cannot be null");
        }
        if (buyer.getFirstName() == null || buyer.getLastName() == null) {
            throw new IllegalArgumentException("not valid buyer fields");
        }
        Buyer existsBuyer = buyerRepository.findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("buyer with id " + uuid + " not found"));
        existsBuyer.setFirstName(trimString(buyer.getFirstName()));
        existsBuyer.setLastName(trimString(buyer.getLastName()));
        existsBuyer.setBirthDate(buyer.getBirthDate());
        existsBuyer.setMiddleName(trimString(buyer.getMiddleName()));
        existsBuyer.setActive(buyer.isActive());
        Buyer updatedBuyer = buyerRepository.save(existsBuyer);
        log.debug("end update buyer by id : {}", uuid);
        return updatedBuyer;
    }

    private String trimString(String string) {
        return string.trim();
    }
}
