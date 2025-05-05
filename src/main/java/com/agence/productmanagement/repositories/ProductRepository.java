package com.agence.productmanagement.repositories;

import java.util.UUID;

import com.agence.productmanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

  void removeById(UUID id);

}
