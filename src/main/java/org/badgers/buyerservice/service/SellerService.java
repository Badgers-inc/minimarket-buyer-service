package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Seller;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    Seller createSeller(Seller seller);

    void deleteSeller(UUID id);

    Seller getSellerById(UUID id);

    Seller updateSellerName(UUID id, String name);

    List<Seller> getAllSellers();

    Seller getSellerByName(String name);

}
