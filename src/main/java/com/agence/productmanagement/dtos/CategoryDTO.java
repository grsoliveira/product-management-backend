package com.agence.productmanagement.dtos;

import java.io.Serializable;
import java.util.UUID;

import com.agence.productmanagement.entities.Category;
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
public class CategoryDTO implements Serializable {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("path")
  private String path;

  @JsonIgnore
  private CategoryDTO parent;

  public static CategoryDTO fromEntity(Category category) {
    if (category == null) {
      return null;
    }

    return CategoryDTO.builder()
        .id(category.getId())
        .name(category.getName())
        .parent(fromEntity(category.getParent()))
        .path(category.getFullPath())
        .build();
  }

}
