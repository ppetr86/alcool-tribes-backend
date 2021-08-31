package com.greenfoxacademy.springwebapp.location.services;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.greenfoxacademy.springwebapp.location.services.GenericSpecification.SearchCriteria;
import static com.greenfoxacademy.springwebapp.location.services.GenericSpecification.SearchOperation;

class GenericSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;
    private final List<Specification<T>> specifications;

    public GenericSpecificationsBuilder() {
        this.params = new ArrayList<>();
        this.specifications = new ArrayList<>();
    }

    public Specification<T> build() {
        Specification<T> result = null;
        if (!params.isEmpty()) {
            result = new GenericSpecification<>(params.get(0));
            for (int index = 1; index < params.size(); ++index) {
                SearchCriteria searchCriteria = params.get(index);
                result = searchCriteria.isOrOperation() ? Specification.where(result).or(new GenericSpecification<>(searchCriteria))
                        : Specification.where(result).and(new GenericSpecification<>(searchCriteria));
            }
        }
        if (!specifications.isEmpty()) {
            int index = 0;
            if (Objects.isNull(result)) {
                result = specifications.get(index++);
            }
            for (; index < specifications.size(); ++index) {
                result = Specification.where(result).and(specifications.get(index));
            }
        }
        return result;
    }

    public final GenericSpecificationsBuilder<T> with(final String key, final SearchOperation searchOperation, final boolean isOrOperation, final List<Object> arguments) {
        params.add(new SearchCriteria(key, searchOperation, isOrOperation, arguments));
        return this;
    }

    public final GenericSpecificationsBuilder<T> with(Specification<T> specification) {
        specifications.add(specification);
        return this;
    }

    public final GenericSpecificationsBuilder<T> with(final String key, final SearchOperation searchOperation, final List<Object> arguments) {
        return with(key, searchOperation, false, arguments);
    }
}
