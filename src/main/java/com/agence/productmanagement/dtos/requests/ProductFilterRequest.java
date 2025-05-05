package com.agence.productmanagement.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterRequest {
  private String name;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private UUID categoryId;
}
