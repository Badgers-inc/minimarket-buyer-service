package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.dto.filter.BuyerFilter;
import org.badgers.buyerservice.dto.filter.BuyerSpecifications;
import org.badgers.buyerservice.entity.Buyer;
import org.badgers.buyerservice.mapper.BuyerMapper;
import org.badgers.buyerservice.repository.BuyerRepository;
import org.badgers.buyerservice.service.BuyerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    private final BuyerMapper buyerMapper;
    private final BuyerRepository buyerRepository;

    @Override
    @Transactional
    public BuyerResponseDto save(BuyerRequestDto buyer) {
        log.debug("начало сохранения покупателя : {}", buyer);
        if (buyer == null) {
            throw new IllegalArgumentException("покупатель не может быть null");
        }
        if (buyer.firstName() == null || buyer.lastName() == null) {
            throw new IllegalArgumentException("не валидные свойства покупателя");
        }
        Buyer entityBuyer = buyerMapper.buyerRequestToBuyer(buyer);
        Buyer savedBuyer = buyerRepository.save(entityBuyer);
        log.debug("окончание сохранения покупателя : {}", savedBuyer);
        return buyerMapper.buyerToBuyerResponseDto(savedBuyer);
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        log.debug("начало деактивации покупателя : {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (!buyer.isActive()) {
            log.debug("покупатель {} должен быть активным для проведения деактивации", id);
            return;
        }
        buyer.setActive(false);
        log.debug("покупатель успешно деактивирован : {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyerResponseDto findById(UUID id) {
        log.debug("начат поиск покупателя по id : {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("покупатель с id " + id + " не найден"));
        log.debug("поиск покупателя с id: {} завершен", buyer);
        return buyerMapper.buyerToBuyerResponseDto(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponseDto> findAll() {
        log.debug("начат поиск всех покупателей");
        List<Buyer> buyers = buyerRepository.findAll();
        log.debug("поиск всех покупателей завершен");
        return buyers.stream().map(buyerMapper::buyerToBuyerResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponseDto> findAllByFilter(BuyerFilter filter) {
        log.debug("начат поиск всех покупателей по фильтру");
        Specification<Buyer> specification = BuyerSpecifications.buildSpecification(filter);
        List<Buyer> buyers = buyerRepository.findAll(specification);
        log.debug("поиск всех покупателей по фильтру завершен");
        return buyers.stream().map(buyerMapper::buyerToBuyerResponseDto).toList();
    }

    @Override
    @Transactional
    public Page<BuyerResponseDto> findAllByFilter(BuyerFilter filter, Pageable page) {
        log.debug("начат поиск всех покупателей по фильтру");
        Specification<Buyer> specification = BuyerSpecifications.buildSpecification(filter);
        Page<Buyer> buyerPage = buyerRepository.findAll(specification, page);
        log.debug("поиск всех покупателей по филтру завершен");
        return buyerPage.map(buyerMapper::buyerToBuyerResponseDto);
    }

    @Override
    @Transactional
    public BuyerResponseDto updateById(UUID uuid, BuyerRequestDto buyer) {
        log.debug("начато обновление покупателя по id : {}", uuid);
        Buyer existingBuyer = buyerRepository.findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("buyer with id " + uuid + " not found"));
        if (buyer.firstName() != null) {
            existingBuyer.setFirstName(trimString(buyer.firstName()));
        }
        if (buyer.lastName() != null) {
            existingBuyer.setLastName(trimString(buyer.lastName()));
        }
        if (buyer.middleName() != null) {
            existingBuyer.setMiddleName(trimString(buyer.middleName()));
        }
        if (buyer.birthDate() != null) {
            existingBuyer.setBirthDate(buyer.birthDate());
        }
        return buyerMapper.buyerToBuyerResponseDto(existingBuyer);
    }

    private String trimString(String string) {
        return string.trim();
    }
}

