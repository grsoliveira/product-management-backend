package com.agence.productmanagement.repositories.specs;

import java.util.ArrayList;
import java.util.List;

import com.agence.productmanagement.dtos.requests.ProductFilterRequest;
import com.agence.productmanagement.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
  public static Specification<Product> withFilters(ProductFilterRequest filter) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getName() != null && !filter.getName().isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
      }

      if (filter.getMinPrice() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
      }

      if (filter.getMaxPrice() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
      }

      if (filter.getCategoryId() != null) {
        predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
