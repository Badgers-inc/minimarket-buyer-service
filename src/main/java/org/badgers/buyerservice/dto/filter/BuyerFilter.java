package org.badgers.buyerservice.dto.filter;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BuyerFilter(
        @Size(max = 50, message = "First name too long")
        String firstName,
        @Size(max = 50, message = "Last name too long")
        String lastName,
        @Size(max = 50, message = "Middle name too long")
        String middleName,
        Boolean active,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Past(message = "Birth date must be in the past")
        LocalDate birthDateFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Past(message = "Birth date must be in the past")
        LocalDate birthDateTo
) {}