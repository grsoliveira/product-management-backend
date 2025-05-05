package com.agence.productmanagement.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.agence.productmanagement.dtos.CategoryDTO;
import com.agence.productmanagement.dtos.ProductDTO;
import com.agence.productmanagement.dtos.requests.ProductCreateUpdateRequest;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.repositories.CategoryRepository;
import com.agence.productmanagement.repositories.ProductRepository;
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

  public Product findById(UUID uuid) {
    return productRepository.findById(uuid)
        .orElseThrow(() ->
            new ResponseStatusException(NOT_FOUND, "Product not found for uuid " + uuid));
  }

  public String delete(UUID uuid) {
    this.productRepository.removeById(uuid);
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

  public Product create(@Valid ProductCreateUpdateRequest request) {
    Category category = null;

    if (request.getCategoryId() != null) {
      category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found for id " + request.getCategoryId()));
    }

    Product product = new Product();
    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setCategory(category);

    return productRepository.save(product);
  }

  public Product update(String productId, @Valid ProductCreateUpdateRequest request) {
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

    if (request.getCategoryId() != null) {
      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found for id " + request.getCategoryId()));
      existingProduct.setCategory(category);
    }

    return productRepository.save(existingProduct);
  }

}
