package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public final class LocationSpecifications {

    static Specification<LocationEntity> findLocationByType(LocationType type) {
        return new Specification<LocationEntity>() {
            @Override
            public Predicate toPredicate(Root<LocationEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("type"), type);
            }
        };
    }

    static Specification<LocationEntity> hasIntegerAttributeEqual(int x, String attribute) {
        return (root, query, cb) -> cb.equal(root.get(attribute), x);
    }

    static Specification<LocationEntity> hasXBiggerThan(int x) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("x"), x);
    }
}
