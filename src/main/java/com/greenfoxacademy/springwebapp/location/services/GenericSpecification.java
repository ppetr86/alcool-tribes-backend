package com.greenfoxacademy.springwebapp.location.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    @Data
    @AllArgsConstructor
    public static class SearchCriteria {

        private String key;
        private SearchOperation searchOperation;
        private boolean isOrOperation;
        private List<Object> arguments;
    }

    public enum SearchOperation {

        EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, IN
    }

    private SearchCriteria searchCriteria;


    public GenericSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        List<Object> arguments = searchCriteria.getArguments();
        Object arg = arguments.get(0);

        switch (searchCriteria.getSearchOperation()) {
            case EQUALITY:
                return cb.equal(root.get(searchCriteria.getKey()), arg);
            case GREATER_THAN:
                return cb.greaterThan(root.get(searchCriteria.getKey()), (Comparable) arg);
            case IN:
                return root.get(searchCriteria.getKey()).in(arguments);
            case NEGATION:
                return cb.not(root.get(searchCriteria.getKey()));
            case LESS_THAN:
                return cb.lessThan(root.get(searchCriteria.getKey()), (Comparable) arg);
            case LIKE:
                return criteriaQuery.where(cb.like(root.<String>get("type"), "%" + arg + "%")).getRestriction();
            case STARTS_WITH:
                return criteriaQuery.where(cb.like(root.<String>get("type"), arg + "%")).getRestriction();
        }
        return null;
    }
}