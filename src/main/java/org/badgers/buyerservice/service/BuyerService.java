package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Buyer;

import java.util.List;
import java.util.UUID;

public interface BuyerService {

    Buyer save(Buyer buyer);

    void delete(UUID uuid);

    Buyer findById(UUID id);

    List<Buyer> findAll();

    Buyer updateById(UUID uuid, Buyer buyer);

}
