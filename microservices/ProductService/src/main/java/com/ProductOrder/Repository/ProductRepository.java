package com.ProductOrder.Repository;

import com.ProductOrder.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}