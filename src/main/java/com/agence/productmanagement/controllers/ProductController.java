package com.agence.productmanagement.controllers;

import java.util.List;
import java.util.UUID;

import com.agence.productmanagement.dtos.ProductDTO;
import com.agence.productmanagement.dtos.ProductToListDTO;
import com.agence.productmanagement.dtos.requests.ProductCreateUpdateRequest;
import com.agence.productmanagement.dtos.requests.ProductFilterRequest;
import com.agence.productmanagement.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/product")
@RestController
@AllArgsConstructor
public class ProductController {

  private ProductService productService;

  @GetMapping("/{productId}")
  @Operation(summary = "Get a product by Id")
  public ResponseEntity<ProductDTO> find(@PathVariable String productId) {
    return ResponseEntity.ok(productService.findById(UUID.fromString(productId)));
  }

  @GetMapping
  @Operation(summary = "Get all products ")
  public ResponseEntity<List<ProductToListDTO>> findById() {
    return ResponseEntity.ok(productService.list());
  }

  @DeleteMapping("/{productId}")
  @Operation(summary = "Delete a product by Id")
  public ResponseEntity<String> delete(@PathVariable String productId) {
    return ResponseEntity.ok(productService.delete(UUID.fromString(productId)));
  }

  @PostMapping
  @Operation(summary = "Create a new product")
  public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductCreateUpdateRequest request) {
    ProductDTO product = productService.create(request);
    return new ResponseEntity<>(product, HttpStatus.CREATED);
  }

  @PutMapping("/{productId}")
  @Operation(summary = "Updates an existing product")
  public ResponseEntity<ProductDTO> update(@PathVariable String productId,
                                           @RequestBody @Valid ProductCreateUpdateRequest request) {
    ProductDTO product = productService.update(productId, request);
    return new ResponseEntity<>(productService.findById(product.getId()), HttpStatus.OK);
  }

  @GetMapping("/search")
  @Operation(summary = "Return a list of filtered products")
  public ResponseEntity<List<ProductDTO>> search(@Valid ProductFilterRequest request) {
    List<ProductDTO> result = productService.search(request);
    return ResponseEntity.ok(result);
  }
}
