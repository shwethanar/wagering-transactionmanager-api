package com.tabcorp.transactionmanagementapi.service;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.models.Customer;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.repository.CustomerRepository;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
import com.tabcorp.transactionmanagementapi.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class TransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public String addTransaction(TransactionRequest transactionRequest) {
    	
        // Retrieve customer and product
        Customer customer = customerRepository.findById(transactionRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Product product = productRepository.findById(transactionRequest.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionTime(transactionRequest.getTransactionTime());
        transaction.setCustomerId(customer.getCustomerId());// Set IDs for customer 
        transaction.setProductCode(product.getProductCode());// Set product code for Product
        transaction.setQuantity(transactionRequest.getQuantity());

        transactionRepository.save(transaction);

        return "Transaction added successfully";
    }
}
