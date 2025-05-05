package com.agence.productmanagement.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.agence.productmanagement.entities.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("category")
  private CategoryDTO category;
}
