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
public class ProductToListDTO {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("available")
  private Boolean available;

  @JsonProperty("price")
  private BigDecimal price;

  @JsonProperty("category")
  private String category;

  @JsonIgnore
  public static ProductToListDTO fromEntity(Product product) {
    return ProductToListDTO.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .available(product.getAmount() != null && product.getAmount().intValue() > 0 ? Boolean.TRUE : Boolean.FALSE )
        .price(product.getPrice())
        //TODO fix category full path
        .category(product.getCategory() != null
            ? ""
            : null)
        .build();
  }
}
