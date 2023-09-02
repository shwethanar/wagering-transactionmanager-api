package com.tabcorp.transactionmanagementapi.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.CustomerNotFoundException;
import com.tabcorp.transactionmanagementapi.errorhandling.ProductNotFoundException;
import com.tabcorp.transactionmanagementapi.models.Customer;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.repository.CustomerRepository;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
import com.tabcorp.transactionmanagementapi.repository.TransactionRepository;
import com.tabcorp.transactionmanagementapi.service.TransactionService;
import com.tabcorp.transactionmanagementapi.validator.TransactionValidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = { "spring.profiles.active=test" })
@ActiveProfiles("test") // Use the test profile
//@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)

public class TransactionServiceTest {

   @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionRepository transactionRepository;

     
    @Mock
    private TransactionValidator transactionValidator;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        
    }
    
   
    @Test
    public void testAddTransaction_ProductNotFound() {
    	
    	// Create mock repositories
        CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);

        // Configure mock behavior for findById
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(createMockCustomer()));

        // Create the transaction service with the mocked repositories
        transactionService = new TransactionService(customerRepository, productRepository, null, null);
   
        // Create a mock transaction request
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCustomerId(1L);
        transactionRequest.setProductCode("PRODUCT_002"); // This product doesn't exist
        transactionRequest.setQuantity(5);

        // Now, you should expect a ProductNotFoundException to be thrown
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            transactionService.addTransaction(transactionRequest);
        });
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setLocation("Australia");
        // Set other properties as needed for testing
        return customer;
    }
    
    @Test
    public void testAddTransaction_Success() {
        // Create a mock customer and product
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(1L);
        mockCustomer.setLocation("Australia");

        Product mockProduct = new Product();
        mockProduct.setProductCode("PRODUCT_001");

        // Create a mock transaction request
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCustomerId(1L);
        transactionRequest.setProductCode("PRODUCT_001");
        transactionRequest.setQuantity(5);

        // Mock repository calls
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        Mockito.when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(mockProduct));

        // Create a mock transactionValidator
        TransactionValidator transactionValidator = Mockito.mock(TransactionValidator.class);

        // Mock transaction validation
        Mockito.doNothing().when(transactionValidator).validate(transactionRequest, mockProduct);

        // Create the TransactionService with the mock transactionValidator
        TransactionService transactionService = new TransactionService(customerRepository, productRepository,transactionRepository,  transactionValidator);

        // Capture the argument passed to transactionRepository.save()
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);

        // When transactionRepository.save() is called, capture the transaction and return a predefined value
        Mockito.doAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            return savedTransaction;
        }).when(transactionRepository).save(transactionArgumentCaptor.capture());

        // Add the transaction
        Transaction addedTransaction = transactionService.addTransaction(transactionRequest);

        // Verify the added transaction
        assertEquals(1L, addedTransaction.getId());
        assertEquals(1L, addedTransaction.getCustomerId());
        assertEquals("PRODUCT_001", addedTransaction.getProductCode());
        assertEquals(5, addedTransaction.getQuantity());
        assertTrue(LocalDateTime.now().getDayOfYear() == addedTransaction.getTransactionTime().getDayOfYear());

        // Verify that transactionRepository.save() was called with the correct argument
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        assertEquals(1L, capturedTransaction.getCustomerId());
        assertEquals("PRODUCT_001", capturedTransaction.getProductCode());
        assertEquals(5, capturedTransaction.getQuantity());
    }

 

   
    
    @Test
    public void testAddTransaction_CustomerNotFound() {
        // Create a transaction request with a non-existent customer ID
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(999L); // Non-existent customer ID
        request.setProductCode("PRODUCT_001");
        request.setQuantity(2);

        // Mock the customer repository to return Optional.empty() for the customer
      //  Mockito.when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Test the addTransaction method and expect a CustomerNotFoundException
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> transactionService.addTransaction(request));

        // Verify the exception message
        assertEquals("Customer not found for ID: 999", exception.getMessage());
    }

    
    // Add more test cases for validation, edge cases, and other scenarios as needed
}
