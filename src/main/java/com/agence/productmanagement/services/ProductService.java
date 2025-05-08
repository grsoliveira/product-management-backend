package com.agence.productmanagement.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.agence.productmanagement.dtos.CategoryDTO;
import com.agence.productmanagement.dtos.ProductDTO;
import com.agence.productmanagement.dtos.requests.ProductCreateUpdateRequest;
import com.agence.productmanagement.dtos.requests.ProductFilterRequest;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.repositories.CategoryRepository;
import com.agence.productmanagement.repositories.ProductRepository;
import com.agence.productmanagement.repositories.specs.ProductSpecifications;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {
  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;

  protected Product find(UUID uuid) {
    return productRepository.findById(uuid)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, "Product not found for uuid " + uuid));
  }

  public ProductDTO findById(UUID uuid) {
    Product product = this.find(uuid);
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

  public String delete(UUID uuid) {
    this.productRepository.delete(this.find(uuid));
    return "Product removed successfully";
  }

  public List<ProductDTO> list() {
    List<Product> products = StreamSupport
        .stream(productRepository.findAll().spliterator(), false)
        .collect(Collectors.toList());

    List<ProductDTO> productDTOs = products.stream()
        .map(product -> ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .category(product.getCategory() != null
                ? CategoryDTO.builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .build()
                : null)
            .build())
        .collect(Collectors.toList());

    return productDTOs;
  }

  public ProductDTO create(ProductCreateUpdateRequest request) {
    Category category = null;

    if (request.getCategory() != null) {
      category = categoryRepository.findById(UUID.fromString(request.getCategory().getId()))
          .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found for id " + request.getCategory()));
    }

    Product product = new Product();
    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setCategory(category);

    Product saved = productRepository.save(product);
    return ProductDTO.builder()
        .id(saved.getId())
        .name(saved.getName())
        .price(saved.getPrice())
        .category(saved.getCategory() != null
            ? CategoryDTO.builder()
            .id(saved.getCategory().getId())
            .name(saved.getCategory().getName())
            .build()
            : null)
        .build();
  }

  public ProductDTO update(String productId, ProductCreateUpdateRequest request) {
    UUID uuid;
    try {
      uuid = UUID.fromString(productId);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(NOT_FOUND, "Invalid UUID format: " + productId);
    }

    Product existingProduct = productRepository.findById(uuid)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found for uuid " + productId));

    if (request.getName() != null) {
      existingProduct.setName(request.getName());
    }

    if (request.getPrice() != null) {
      existingProduct.setPrice(request.getPrice());
    }

    if (request.getCategory() != null) {
      Category category = categoryRepository.findById(UUID.fromString(request.getCategory().getId()))
          .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found for id " + request.getCategory()));
      existingProduct.setCategory(category);
    }

    Product saved = productRepository.save(existingProduct);
    return ProductDTO.builder()
        .id(saved.getId())
        .name(saved.getName())
        .price(saved.getPrice())
        .category(saved.getCategory() != null
            ? CategoryDTO.builder()
            .id(saved.getCategory().getId())
            .name(saved.getCategory().getName())
            .build()
            : null)
        .build();
  }

  public List<ProductDTO> search(ProductFilterRequest filter) {
    List<Product> products = productRepository.findAll(ProductSpecifications.withFilters(filter));
    return products.stream().map(ProductDTO::fromEntity).toList();
  }

}
