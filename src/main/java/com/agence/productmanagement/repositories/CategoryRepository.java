package com.agence.productmanagement.repositories;

import java.util.UUID;

import com.agence.productmanagement.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, UUID> {

}
