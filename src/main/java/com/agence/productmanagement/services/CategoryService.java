package com.agence.productmanagement.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.agence.productmanagement.dtos.CategoryDTO;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService {
  private CategoryRepository categoryRepository;

  private Category find(UUID uuid) {
    return categoryRepository.findById(uuid)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, "Category not found for uuid " + uuid));
  }

  public CategoryDTO findById(UUID uuid) {
    Category category = this.find(uuid);
    return CategoryDTO.builder()
        .id(category.getId())
        .name(category.getName())
        .parent(category.getParent() != null ?
            CategoryDTO.builder()
                .id(category.getParent().getId())
                .name(category.getParent().getName())
                .build() : null)
        .build();
  }

  public List<CategoryDTO> list() {
    List<Category> categories = StreamSupport
        .stream(categoryRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());

    List<CategoryDTO> categoryDTOs = categories.stream()
        .map(category -> CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .parent(category.getParent() != null ?
                CategoryDTO.builder()
                    .id(category.getParent().getId())
                    .name(category.getParent().getName())
                    .build() : null)
            .build())
        .collect(Collectors.toList());

    return categoryDTOs;
  }
}
