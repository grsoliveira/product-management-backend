package com.agence.productmanagement.repositories;

import java.util.UUID;

import com.agence.productmanagement.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {

}
