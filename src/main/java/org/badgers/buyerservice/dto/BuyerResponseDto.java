package org.badgers.buyerservice.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BuyerResponseDto(
        UUID uuid,
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthDate,
        Instant createdAt,
        Instant updatedAt,
        boolean active
) {
}
