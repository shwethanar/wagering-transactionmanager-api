package com.tabcorp.transactionmanagementapi.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tabcorp.transactionmanagementapi.models.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	// Find transactions by customer ID
    List<Transaction> findByCustomerId(Long customerId);

    // Find transactions by product code
    List<Transaction> findByProductCode(String productCode);
    
}