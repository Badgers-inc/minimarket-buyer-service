package org.badgers.buyerservice.dto.filter;

import org.badgers.buyerservice.entity.Buyer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BuyerSpecifications {

    public static Specification<Buyer> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Buyer> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastName")), lastName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Buyer> hasMiddleName(String middleName) {
        return (root, query, criteriaBuilder) -> {
            if (middleName == null || middleName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("middleName")), middleName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Buyer> birthDateFrom(LocalDate from) {
        return ((root, query, criteriaBuilder) -> {
            if (from == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("birthDate"), from);
        });
    }

    public static Specification<Buyer> birthDateTo(LocalDate to) {
        return (root, query, criteriaBuilder) ->  {
            if (to == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThan(root.get("birthDate"), to);
        };
    }

    public static Specification<Buyer> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->  {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Buyer> buildSpecification(BuyerFilter filter) {
        return Specification
                .where(hasFirstName(filter.firstName()))
                .and(hasLastName(filter.lastName()))
                .and(hasMiddleName(filter.middleName()))
                .and(birthDateFrom(filter.birthDateFrom()))
                .and(birthDateTo(filter.birthDateTo()))
                .and(isActive(filter.active()));
    }
}