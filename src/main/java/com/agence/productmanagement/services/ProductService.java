package com.agence.productmanagement.services;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.agence.productmanagement.dto.CategoryDTO;
import com.agence.productmanagement.dto.ProductDTO;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {
  private ProductRepository productRepository;

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

}
