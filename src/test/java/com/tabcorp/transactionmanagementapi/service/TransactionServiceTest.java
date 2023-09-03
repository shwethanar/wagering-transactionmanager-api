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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest(properties = { "spring.profiles.active=test" })
//@ActiveProfiles("test") // Use the test profile
//@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class TransactionServiceTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionValidator transactionValidator;

    @BeforeEach
    public void setUp() {
        // You can perform any setup that's required for your specific test environment.
    }

    //@Test
    public void testAddTransaction_ProductNotFound() {
        // Create a mock customer
        Customer mockCustomer = createMockCustomer(1L);

        // Create the transaction service
        TransactionService transactionService = new TransactionService(customerRepository, productRepository, transactionRepository, transactionValidator);

        // Create a mock transaction request
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCustomerId(1L);
        transactionRequest.setProductCode("PRODUCT_002"); // This product doesn't exist
        transactionRequest.setQuantity(5);

        // Now, you should expect a ProductNotFoundException to be thrown
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            transactionService.addTransaction(transactionRequest);
        });

        // Verify the exception message
        assertEquals("Product not found for code: PRODUCT_002", exception.getMessage());
    }

    // ... Other test methods ...

    // Helper methods to create mock objects
    private Customer createMockCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setLocation("Australia");
        // Set other properties as needed for testing
        return customer;
    }

    private Product createMockProduct(String productCode) {
        Product product = new Product();
        product.setProductCode(productCode);
        // Set other properties as needed for testing
        return product;
    }

    private Transaction createMockTransaction(Long customerId, String productCode) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setProductCode(productCode);
        // Set other properties as needed for testing
        return transaction;
    }
}
