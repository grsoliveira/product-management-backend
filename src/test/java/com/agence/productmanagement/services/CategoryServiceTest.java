package com.agence.productmanagement.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.agence.productmanagement.entities.Category;
import com.agence.productmanagement.repositories.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  private UUID categoryId;
  private Category category;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    categoryId = UUID.fromString("10000000-0000-0000-0000-000000000001");
    category = new Category();
    category.setId(categoryId);
    category.setName("Eletrônicos");
  }

  @Test
  void testFindById_returnsCategory() {
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    Category result = categoryService.findById(categoryId);

    assertNotNull(result);
    assertEquals("Eletrônicos", result.getName());
    assertEquals(categoryId, result.getId());
    verify(categoryRepository, times(1)).findById(categoryId);
  }

  @Test
  void testFindById_notFound_throwsException() {
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> {
      categoryService.findById(categoryId);
    });

    assertTrue(exception.getMessage().contains("Category not found for uuid"));
  }
}
