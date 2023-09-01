package com.tabcorp.transactionmanagementapi.service;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.CustomerNotFoundException;
import com.tabcorp.transactionmanagementapi.errorhandling.ProductNotFoundException;
import com.tabcorp.transactionmanagementapi.models.Customer;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.repository.CustomerRepository;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
import com.tabcorp.transactionmanagementapi.repository.TransactionRepository;
import com.tabcorp.transactionmanagementapi.validator.TransactionValidator;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class TransactionService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    @Autowired
    public TransactionService(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            TransactionRepository transactionRepository,
            TransactionValidator transactionValidator) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    public String addTransaction(TransactionRequest transactionRequest) {
       

        // Retrieve customer and product
        Customer customer = customerRepository.findById(transactionRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Product product = productRepository.findById(transactionRequest.getProductCode())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // Validate the transaction request
        transactionValidator.validate(transactionRequest,product);
        
        // Create and save the transaction
        Transaction transaction = new Transaction();

        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setCustomerId(customer.getCustomerId());
        transaction.setProductCode(product.getProductCode());
        transaction.setQuantity(transactionRequest.getQuantity());

        transactionRepository.save(transaction);

        return "Transaction added successfully";
    }
}