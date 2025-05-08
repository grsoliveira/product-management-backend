package com.agence.productmanagement.controllers;

import java.util.List;
import java.util.UUID;

import com.agence.productmanagement.dtos.CategoryDTO;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/category")
@RestController
@AllArgsConstructor
public class CategoryController {

  private CategoryService categoryService;

  @GetMapping("/{categoryId}")
  @Operation(summary = "Get a category by Id")
  public ResponseEntity<CategoryDTO> findById(@PathVariable String categoryId) {
    return ResponseEntity.ok(categoryService.findById(UUID.fromString(categoryId)));
  }

  @GetMapping()
  @Operation(summary = "Get all category ")
  public ResponseEntity<List<CategoryDTO>> findById() {
    return ResponseEntity.ok(categoryService.list());
  }

}
