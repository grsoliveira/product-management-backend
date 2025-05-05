package com.agence.productmanagement.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.agence.productmanagement.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  public static ProductDTO fromEntity(Product product) {
    return ProductDTO.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .category(product.getCategory() != null
            ? CategoryDTO.builder()
            .id(product.getCategory().getId())
            .name(product.getCategory().getName())
            .build()
            : null)
        .build();
  }
}
