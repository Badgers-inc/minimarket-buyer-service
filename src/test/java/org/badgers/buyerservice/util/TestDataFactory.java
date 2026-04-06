package org.badgers.buyerservice.util;

import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.filter.BuyerFilter;
import org.badgers.buyerservice.entity.Buyer;

import java.time.LocalDate;
import java.util.UUID;

public class TestDataFactory {

    public static Buyer createDefaultBuyer() {
        Buyer buyer = new Buyer();
        buyer.setId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        buyer.setFirstName("firstName");
        buyer.setLastName("lastName");
        buyer.setBirthDate(LocalDate.of(1997, 12, 05));
        return buyer;
    }

    public static BuyerRequestDto createDefaultBuyerRequestDto() {
        return new BuyerRequestDto(
                "firstName", "lastName", "middleName", LocalDate.of(2000, 12, 20));
    }

    public static BuyerFilter creaeteDefaultBuyerFilter() {
        return new BuyerFilter(
                "firstName", "lastName", "middleName", true,
                LocalDate.of(2000, 12, 20), LocalDate.of(2222, 12, 20));
    }
}
