package com.greenfoxacademy.springwebapp.specification;

import com.greenfoxacademy.springwebapp.specification.GenericSpecification.SearchOperation;
import java.util.Collections;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationFactory<T> {

    public Specification<T> isEqual(String key, Object arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.EQUALITY, Collections.singletonList(arg)).build();
    }

    public Specification<T> isGreaterThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.GREATER_THAN, Collections.singletonList(arg)).build();
    }

    public Specification<T> isIn(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.IN, Collections.singletonList(arg)).build();
    }

    public Specification<T> isLessThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.LESS_THAN, Collections.singletonList(arg)).build();
    }

    public Specification<T> isLike(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.LIKE, Collections.singletonList(arg)).build();
    }

    public Specification<T> isNegation(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.NEGATION, Collections.singletonList(arg)).build();
    }

    public Specification<T> isStartsWith(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.STARTS_WITH, Collections.singletonList(arg)).build();
    }
}
