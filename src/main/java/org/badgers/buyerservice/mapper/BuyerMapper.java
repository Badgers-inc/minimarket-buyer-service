package org.badgers.buyerservice.mapper;

import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.entity.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuyerMapper {
    @Mapping(target = "uuid", source = "id")
    BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer);
    Buyer buyerRequestToBuyer(BuyerRequestDto buyerRequestDto);
    @Mapping(target = "id", source = "uuid")
    Buyer buyerResponseDtoToBuyer(BuyerResponseDto buyerResponseDto);
}
