package com.agence.productmanagement.dtos.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateUpdateRequest {

  private String name;

  private String description;

  private Integer amount;

  private BigDecimal price;

  private ProductCreateUpdateCategoryRequest category;
}
