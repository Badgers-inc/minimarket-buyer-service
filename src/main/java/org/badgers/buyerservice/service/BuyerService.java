package org.badgers.buyerservice.service;

import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.dto.filter.BuyerFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BuyerService {

    BuyerResponseDto save(BuyerRequestDto buyer);

    void softDelete(UUID uuid);

    BuyerResponseDto findById(UUID id);

    List<BuyerResponseDto> findAllByFilter(BuyerFilter filter);

    Page<BuyerResponseDto> findAllByFilter(BuyerFilter filter, Pageable pageable);

    List<BuyerResponseDto> findAll();

    BuyerResponseDto updateById(UUID uuid, BuyerRequestDto buyer);

}
