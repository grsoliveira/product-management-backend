package com.agence.productmanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.agence.productmanagement.dtos.ProductDTO;
import com.agence.productmanagement.dtos.ProductToListDTO;
import com.agence.productmanagement.dtos.requests.ProductCreateUpdateCategoryRequest;
import com.agence.productmanagement.dtos.requests.ProductCreateUpdateRequest;
import com.agence.productmanagement.dtos.requests.ProductFilterRequest;
import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.entities.Product;
import com.agence.productmanagement.repositories.CategoryRepository;
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
  @Mock
  private CategoryRepository categoryRepository;

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

    ProductDTO result = productService.findById(productId);

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

    List<ProductToListDTO> result = productService.list();

    assertEquals(1, result.size());
    ProductToListDTO dto = result.get(0);

    assertEquals(product.getId(), dto.getId());
    assertEquals(product.getName(), dto.getName());
    assertEquals(product.getPrice(), dto.getPrice());
    assertNotNull(dto.getCategory());

    verify(productRepository, times(1)).findAll();
  }

  //FIXME refactor
//  @Test
//  void testDelete_callsRepositoryAndReturnsSuccessMessage() {
//    UUID productId = UUID.fromString("20000000-0000-0000-0000-000000000001");
//
//    doNothing().when(productRepository).delete(any(Product.class));
//
//    String result = productService.delete(productId);
//
//    assertEquals("Product removed successfully", result);
//    verify(productRepository, times(1)).removeById(productId);
//  }

  @Test
  void testCreateProduct_successfully() {
    UUID categoryId = category.getId();

    ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
    request.setName("Notebook");
    request.setPrice(new BigDecimal("3500.00"));
    request.setCategory(
        ProductCreateUpdateCategoryRequest.builder()
            .id(categoryId.toString())
            .build());

    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    Product savedProduct = new Product();
    savedProduct.setId(UUID.randomUUID());
    savedProduct.setName(request.getName());
    savedProduct.setPrice(request.getPrice());
    savedProduct.setCategory(category);

    when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class))).thenReturn(savedProduct);

    ProductDTO result = productService.create(request);

    assertNotNull(result);
    assertEquals("Notebook", result.getName());
    assertEquals(new BigDecimal("3500.00"), result.getPrice());
    assertEquals(category.getId(), result.getCategory().getId());

    verify(categoryRepository, times(1)).findById(categoryId);
    verify(productRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Product.class));
  }

  @Test
  void testCreateProduct_categoryNotFound_throwsException() {
    UUID fakeCategoryId = UUID.randomUUID();

    ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
    request.setName("Notebook");
    request.setPrice(new BigDecimal("3500.00"));
    request.setCategory(
        ProductCreateUpdateCategoryRequest.builder()
            .id(fakeCategoryId.toString())
            .build()
    );

    when(categoryRepository.findById(fakeCategoryId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      productService.create(request);
    });

    assertTrue(exception.getMessage().contains("Category not found for id"));
    verify(categoryRepository, times(1)).findById(fakeCategoryId);
    verify(productRepository, times(0)).save(org.mockito.ArgumentMatchers.any(Product.class));
  }

  @Test
  void testUpdateProduct_successfully() {
    UUID categoryId = category.getId();

    ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
    request.setName("Smart TV");
    request.setPrice(new BigDecimal("2999.99"));
    request.setCategory(
        ProductCreateUpdateCategoryRequest.builder()
            .id(categoryId.toString())
            .build()
    );

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ProductDTO updated = productService.update(productId.toString(), request);

    assertNotNull(updated);
    assertEquals("Smart TV", updated.getName());
    assertEquals(new BigDecimal("2999.99"), updated.getPrice());
    assertEquals(categoryId, updated.getCategory().getId());

    verify(productRepository, times(1)).findById(productId);
    verify(categoryRepository, times(1)).findById(categoryId);
    verify(productRepository, times(1)).save(product);
  }

  @Test
  void testUpdateProduct_productNotFound_throwsException() {
    ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
    request.setName("Smart TV");
    request.setPrice(new BigDecimal("2999.99"));
    request.setCategory(
        ProductCreateUpdateCategoryRequest.builder()
            .id(category.getId().toString())
            .build()
    );

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      productService.update(productId.toString(), request);
    });

    assertTrue(exception.getMessage().contains("Product not found for uuid"));
    verify(productRepository, times(1)).findById(productId);
    verify(categoryRepository, times(0)).findById(org.mockito.ArgumentMatchers.any());
    verify(productRepository, times(0)).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testUpdateProduct_categoryNotFound_throwsException() {
    UUID fakeCategoryId = UUID.randomUUID();

    ProductCreateUpdateRequest request = new ProductCreateUpdateRequest();
    request.setName("Smart TV");
    request.setPrice(new BigDecimal("2999.99"));
    request.setCategory(
        ProductCreateUpdateCategoryRequest.builder()
            .id(fakeCategoryId.toString())
            .build());

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(categoryRepository.findById(fakeCategoryId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      productService.update(productId.toString(), request);
    });

    assertTrue(exception.getMessage().contains("Category not found for id"));
    verify(productRepository, times(1)).findById(productId);
    verify(categoryRepository, times(1)).findById(fakeCategoryId);
    verify(productRepository, times(0)).save(org.mockito.ArgumentMatchers.any());
  }


}
