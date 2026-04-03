package org.badgers.buyerservice.service.impl;

import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.dto.filter.BuyerFilter;
import org.badgers.buyerservice.entity.Buyer;
import org.badgers.buyerservice.mapper.BuyerMapper;
import org.badgers.buyerservice.repository.BuyerRepository;
import org.badgers.buyerservice.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceImplTest {

    private final Buyer buyer = TestDataFactory.createDefaultBuyer();
    private final BuyerRequestDto buyerRequestDto = TestDataFactory.createDefaultBuyerRequestDto();
    private final BuyerFilter buyerFilter = TestDataFactory.creaeteDefaultBuyerFilter();

    @Spy
    BuyerMapper buyerMapper = Mappers.getMapper(BuyerMapper.class);

    @Mock
    BuyerRepository buyerRepository;

    @InjectMocks
    BuyerServiceImpl buyerService;

    @Test
    void testFindBuyerById() {
        Mockito.when(buyerRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(buyer));
        BuyerResponseDto inspectedBuyer = buyerService.findById(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        Assertions.assertEquals(buyer.getId(), inspectedBuyer.uuid());
        Assertions.assertEquals(buyer.getFirstName(), inspectedBuyer.firstName());
        Assertions.assertEquals(buyer.getLastName(), inspectedBuyer.lastName());
    }

    @Test
    void testCreateBuyer() {
        Mockito.when(buyerRepository.save(Mockito.any(Buyer.class))).thenReturn(buyer);
        BuyerResponseDto inspectedBuyer = buyerService.save(buyerRequestDto);
        Assertions.assertEquals(buyer.getId(), inspectedBuyer.uuid());
        Assertions.assertEquals(buyer.getFirstName(), inspectedBuyer.firstName());
        Assertions.assertThrows(IllegalArgumentException.class, () -> buyerService.save(null));
    }

    @Test
    void testSoftDeleteWithException() {
        Mockito.when(buyerRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(buyer));
        buyerService.softDelete(buyer.getId());
        Mockito.verify(buyerRepository, Mockito.times(1)).findById(Mockito.any());
        Assertions.assertThrows(IllegalArgumentException.class, () -> buyerService.softDelete(null));
    }

    @Test
    void testFindAll() {
        Mockito.when(buyerRepository.findAll()).thenReturn(java.util.Arrays.asList(buyer));
        List<BuyerResponseDto> inspectedBuyer = buyerService.findAll();
        Assertions.assertEquals(inspectedBuyer.get(0).firstName(), "firstName");
        Assertions.assertEquals(inspectedBuyer.get(0).lastName(), "lastName");
        Assertions.assertEquals(inspectedBuyer.get(0).birthDate(), LocalDate.of(1997, 12, 05));
        Mockito.verify(buyerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdateBuyerById() {
        Mockito.when(buyerRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(buyer));
        BuyerRequestDto buyerRequestDto = new BuyerRequestDto(
                "firstNameNew", "lastNameNew", "middleNameNew", LocalDate.of(2222, 12, 20)
        );
        BuyerResponseDto inspectedBuyer = buyerService.updateById(UUID.randomUUID(), buyerRequestDto);
        Assertions.assertEquals(buyerRequestDto.firstName(), inspectedBuyer.firstName());
        Assertions.assertEquals(buyerRequestDto.lastName(), inspectedBuyer.lastName());
        Assertions.assertEquals(buyerRequestDto.birthDate(), inspectedBuyer.birthDate());
        Mockito.verify(buyerRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testFindAllByFilter() {
        Pageable page = PageRequest.of(0, 10);
        Page<Buyer> buyerPage = new PageImpl<>(List.of(buyer), page, 1);
        Mockito.when(buyerRepository.findAll(Mockito.any(Specification.class), Mockito.eq(page)))
                .thenReturn(buyerPage);
        Page<BuyerResponseDto> responses = buyerService.findAllByFilter(buyerFilter, page);
        Assertions.assertEquals(buyer.getFirstName(), responses.getContent().get(0).firstName());
        Assertions.assertEquals(buyer.getLastName(), responses.getContent().get(0).lastName());
        Assertions.assertEquals(buyer.getMiddleName(), responses.getContent().get(0).middleName());
        Assertions.assertEquals(buyer.getBirthDate(), responses.getContent().get(0).birthDate());
        Assertions.assertEquals(buyer.getId(), responses.getContent().get(0).uuid());
    }
}