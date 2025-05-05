package com.agence.productmanagement.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.agence.productmanagement.dtos.CategoryDTO;
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
  private Category parentCategory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    categoryId = UUID.fromString("10000000-0000-0000-0000-000000000001");

    category = new Category();
    category.setId(categoryId);
    category.setName("Eletrônicos");

    parentCategory = new Category();
    parentCategory.setId(UUID.fromString("10000000-0000-0000-0000-000000000002"));
    parentCategory.setName("Produtos");

    category.setParent(parentCategory);
  }

  @Test
  void testFindById_returnsCategory() {
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    CategoryDTO result = categoryService.findById(categoryId);

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

  @Test
  void testList() {
    when(categoryRepository.findAll()).thenReturn(List.of(category));

    List<CategoryDTO> categoryDTOs = categoryService.list();

    assertNotNull(categoryDTOs);
    assertEquals(1, categoryDTOs.size());

    CategoryDTO categoryDTO = categoryDTOs.get(0);
    assertEquals(categoryId, categoryDTO.getId());
    assertEquals("Eletrônicos", categoryDTO.getName());

    assertNotNull(categoryDTO.getParent());
    assertEquals(parentCategory.getId(), categoryDTO.getParent().getId());
    assertEquals("Produtos", categoryDTO.getParent().getName());

    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  void testList_emptyList() {
    when(categoryRepository.findAll()).thenReturn(List.of());

    List<CategoryDTO> categoryDTOs = categoryService.list();

    assertNotNull(categoryDTOs);
    assertTrue(categoryDTOs.isEmpty());

    verify(categoryRepository, times(1)).findAll();
  }
}
