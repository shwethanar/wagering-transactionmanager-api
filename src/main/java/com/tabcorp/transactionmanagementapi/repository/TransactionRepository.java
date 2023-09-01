package com.tabcorp.transactionmanagementapi.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tabcorp.transactionmanagementapi.models.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
}