package com.tabcorp.transactionmanagementapi.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tabcorp.transactionmanagementapi.models.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

	Product save(Product customer);
}
