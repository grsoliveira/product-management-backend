package com.agence.productmanagement.controllers;

import java.util.List;
import java.util.UUID;

import com.agence.productmanagement.dto.ProductDTO;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/product")
@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class ProductController {

  private ProductService productService;

  @GetMapping("/{productId}")
  @Operation(summary = "Get a product by Id")
  public ResponseEntity<Product> findById(@PathVariable String productId) {
    return ResponseEntity.ok(productService.findById(UUID.fromString(productId)));
  }

  @GetMapping()
  @Operation(summary = "Get all products ")
  public ResponseEntity<List<ProductDTO>> findById() {
    return ResponseEntity.ok(productService.list());
  }

  @DeleteMapping("/{productId}")
  @Operation(summary = "Delete a product by Id")
  public ResponseEntity<String> delete(@PathVariable String productId) {
    return ResponseEntity.ok(productService.delete(UUID.fromString(productId)));
  }
}
