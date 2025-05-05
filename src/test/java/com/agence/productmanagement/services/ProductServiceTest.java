package com.agence.productmanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.agence.productmanagement.dto.ProductDTO;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

public class ProductServiceTest {
  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private UUID productId;
  private Product product;
  private Category category;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    productId = UUID.fromString("20000000-0000-0000-0000-000000000001");

    category = new Category();
    category.setId(UUID.fromString("30000000-0000-0000-0000-000000000001"));
    category.setName("Eletrônicos");

    product = new Product();
    product.setId(productId);
    product.setName("Smartphone");
    product.setPrice(new BigDecimal("1999.99"));
    product.setCategory(category);
  }

  @Test
  void testFindById_returnsProduct() {
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    Product result = productService.findById(productId);

    assertNotNull(result);
    assertEquals(productId, result.getId());
    assertEquals("Smartphone", result.getName());
    assertEquals(new BigDecimal("1999.99"), result.getPrice());
    verify(productRepository, times(1)).findById(productId);
  }

  @Test
  void testFindById_notFound_throwsException() {
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      productService.findById(productId);
    });

    assertTrue(exception.getMessage().contains("Product not found for uuid"));
    verify(productRepository, times(1)).findById(productId);
  }

  @Test
  void testList_returnsProductDTOsWithCategory() {
    when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

    List<ProductDTO> result = productService.list();

    assertEquals(1, result.size());
    ProductDTO dto = result.get(0);

    assertEquals(product.getId(), dto.getId());
    assertEquals(product.getName(), dto.getName());
    assertEquals(product.getPrice(), dto.getPrice());
    assertNotNull(dto.getCategory());
    assertEquals(category.getId(), dto.getCategory().getId());
    assertEquals(category.getName(), dto.getCategory().getName());

    verify(productRepository, times(1)).findAll();
  }

  @Test
  void testList_returnsProductDTOsWithoutCategory() {
    product.setCategory(null);

    when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

    List<ProductDTO> result = productService.list();

    assertEquals(1, result.size());
    ProductDTO dto = result.get(0);

    assertEquals(product.getId(), dto.getId());
    assertEquals(product.getName(), dto.getName());
    assertEquals(product.getPrice(), dto.getPrice());
    assertNull(dto.getCategory());

    verify(productRepository, times(1)).findAll();
  }
}
