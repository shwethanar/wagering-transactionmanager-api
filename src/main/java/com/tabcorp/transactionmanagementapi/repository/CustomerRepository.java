package com.tabcorp.transactionmanagementapi.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tabcorp.transactionmanagementapi.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
}