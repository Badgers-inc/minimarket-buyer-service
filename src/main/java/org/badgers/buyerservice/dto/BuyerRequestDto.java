package org.badgers.buyerservice.dto;

import java.time.LocalDate;

public record BuyerRequestDto(
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthDate
) {
}
