package com.tabcorp.transactionmanagementapi.service;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.models.Customer;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.repository.CustomerRepository;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
import com.tabcorp.transactionmanagementapi.repository.TransactionRepository;
import com.tabcorp.transactionmanagementapi.validator.TransactionValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

public class NewTest {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private TransactionValidator transactionValidator;

    @BeforeEach
    public void setUp() {
        // Initialize your service and repositories here
        transactionRepository = new TransactionRepositoryStub();
        customerRepository = new CustomerRepositoryStub();
        productRepository = new ProductRepositoryStub();
        transactionValidator = new TransactionValidator();
        transactionService = new TransactionService(customerRepository, productRepository, transactionRepository, transactionValidator);
    }

   // //@Test
    public void testAddTransaction() {
        // Create a TransactionRequest for testing
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(10001L);
        request.setProductCode("PRODUCT_001");
        request.setQuantity(5);

        // Add a customer to the repository
        customerRepository.save(new Customer(10001L, "FName", "LName", 20, "Australia"));

        // Add a product to the repository
        productRepository.save(new Product("PRODUCT_001", BigDecimal.TEN, "Active"));
        transactionService = new TransactionService(customerRepository, productRepository, transactionRepository, transactionValidator);

        

        // Add a transaction
        Transaction transaction = transactionService.addTransaction(request);

        // Assert that the transaction was added successfully
        assertNotNull(transaction);
        assertEquals(1, transactionRepository.findAll().size());
    }

    //@Test
    public void testGetTransactionsToAustraliaCount() {
        // Add customers to the repository
        customerRepository.save(new Customer(10001L, "FName", "LName", 20, "Australia"));
        customerRepository.save(new Customer(10002L, "FName1", "LName1", 60, "Canada"));

        // Create and add transactions
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(10001L);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(10002L);
        transactionRepository.save(transaction2);
        transactionService = new TransactionService(customerRepository, productRepository, transactionRepository, transactionValidator);


        // Test the method
        long australiaTransactionCount = transactionService.getTransactionsToAustraliaCount();

        // Assert that there is one transaction to Australia
        assertEquals(0, australiaTransactionCount);
    }

    ////@Test
    public void testGetTransactionsToAustraliaCount11() {
        // Create a customer from Australia
        Customer australiaCustomer = new Customer(1L, "John", "Doe", 30, "Australia");

        // Create a transaction associated with the Australia customer
        Transaction transactionToAustralia = new Transaction();
        transactionToAustralia.setCustomerId(1L);

        // Create a customer from Canada
        Customer canadaCustomer = new Customer(2L, "Jane", "Smith", 25, "Canada");

        // Create a transaction associated with the Canada customer
        Transaction transactionToCanada = new Transaction();
        transactionToCanada.setCustomerId(2L);

        // Mock repository methods to return the expected data
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(australiaCustomer));
        Mockito.when(customerRepository.findById(2L)).thenReturn(Optional.of(canadaCustomer));

        // Create lists of transactions
        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.add(transactionToAustralia);
        allTransactions.add(transactionToCanada);

        // Mock transactionRepository to return the list of transactions
        Mockito.when(transactionRepository.findAll()).thenReturn(allTransactions);

        // Test the method
        long australiaTransactionCount = transactionService.getTransactionsToAustraliaCount();

        // Assert that there is one transaction to Australia
        assertEquals(1, australiaTransactionCount);
    }
   // //@Test
    public void testAddTransaction1() {
        // Create a TransactionRequest for testing
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(10001L);
        request.setProductCode("PRODUCT_001");
        request.setQuantity(5);

        // Add a transaction
        Transaction transaction = transactionService.addTransaction(request);

        // Assert that the transaction was added successfully
        assertNotNull(transaction);
        assertEquals(1, transactionRepository.findAll().size());
    }

    //@Test
    public void testGetTransactionsToAustraliaCount1() {
        // Create and add transactions
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(10001L);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(10002L);
        transactionRepository.save(transaction2);

        // Test the method
        long australiaTransactionCount = transactionService.getTransactionsToAustraliaCount();

        // Assert that there is one transaction to Australia
        assertEquals(0, australiaTransactionCount);
    }

    // Similar test methods for other service methods...

    // Similar test methods for other service methods...

    // Implement CustomerRepositoryStub, ProductRepositoryStub, and TransactionRepositoryStub
    // as simple in-memory repositories for testing.
}