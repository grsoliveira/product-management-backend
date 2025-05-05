package com.agence.productmanagement.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.UUID;

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

  public Category findById(UUID uuid) {
    return categoryRepository.findById(uuid)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, "Category not found for uuid " + uuid));
  }
}
