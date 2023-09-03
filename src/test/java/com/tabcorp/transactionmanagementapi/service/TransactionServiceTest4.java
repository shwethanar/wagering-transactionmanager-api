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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest(properties = { "spring.profiles.active=test" })
//@ActiveProfiles("test") // Use the test profile
//@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)

public class TransactionServiceTest4 {

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
    
   
    //@Test
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

        //  ProductNotFoundException to be thrown
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            transactionService.addTransaction(transactionRequest);
        });
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setLocation("Australia");
        return customer;
    }
    
    //@Test
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

 

   
    
    //@Test
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

    //@Test
    public void testGetTransactionsToAustraliaCount() {
        // Create a list of mock transactions where some customers are from Australia
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(createMockTransaction(1L, true)); // Customer from Australia
        mockTransactions.add(createMockTransaction(2L, false)); // Customer not from Australia
        mockTransactions.add(createMockTransaction(3L, true)); // Customer from Australia

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // Call the method under test
        long australiaTransactionCount = transactionService.getTransactionsToAustraliaCount();

        // Verify that the method returns the expected count
        assertEquals(2, australiaTransactionCount);
    }

    //@Test
    public void testGetTotalCostByCustomer() {
        // Create a mock customer and a list of mock transactions for the customer
        Customer mockCustomer = createMockCustomer(1L);
        List<Transaction> mockTransactions = createMockTransactionsForCustomer(mockCustomer);

        // Mock the behavior of customerRepository to return the mock customer
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findByCustomerId(1L)).thenReturn(mockTransactions);

        // Mock the behavior of productRepository to return a mock product
        Mockito.when(productRepository.existsById("PRODUCT_001")).thenReturn(true);

        // Call the method under test
        BigDecimal totalCost = transactionService.getTotalCostByCustomer(1L);

        // Verify that the method returns the expected total cost
        assertEquals(new BigDecimal("150.00"), totalCost);
    }

    //@Test
    public void testGetTotalCostByProduct() {
        // Create a mock product and a list of mock transactions for the product
        Product mockProduct = createMockProduct("PRODUCT_001");
        List<Transaction> mockTransactions = createMockTransactionsForProduct(mockProduct);

        // Mock the behavior of productRepository to return the mock product
        Mockito.when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(mockProduct));

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findByProductCode("PRODUCT_001")).thenReturn(mockTransactions);

        // Call the method under test
        BigDecimal totalCost = transactionService.getTotalCostByProduct("PRODUCT_001");

        // Verify that the method returns the expected total cost
        assertEquals(new BigDecimal("450.00"), totalCost);
    }

    private List<Transaction> createMockTransactionsForProduct(Product product) {
        List<Transaction> mockTransactions = new ArrayList<>();
        // Create mock transactions for the given product
        mockTransactions.add(createMockTransaction(1L, true, product.getProductCode(), 100)); // Transaction 1
        mockTransactions.add(createMockTransaction(2L, true, product.getProductCode(), 200)); // Transaction 2
        mockTransactions.add(createMockTransaction(3L, true, product.getProductCode(), 150)); // Transaction 3
        return mockTransactions;
    }
    private Transaction createMockTransaction(Long customerId, boolean isCustomerFromAustralia, String productCode, int quantity) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setProductCode(productCode);
        transaction.setQuantity(quantity);
        // Set other properties as needed for testing
        return transaction;
    }

    private Product createMockProduct(String productCode) {
        Product product = new Product();
        product.setProductCode(productCode);
        // Set other properties as needed for testing
        return product;
    }

    //@Test
    public void testGetTotalCostForCustomers() {
        // Create a list of mock transactions for different customers
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(createMockTransaction(1L, true)); // Customer 1 from Australia
        mockTransactions.add(createMockTransaction(2L, false)); // Customer 2 not from Australia
        mockTransactions.add(createMockTransaction(1L, true)); // Another transaction for Customer 1 from Australia

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // Call the method under test
        Map<Long, BigDecimal> totalCostByCustomer = transactionService.getTotalCostForCustomers();

        // Verify that the method returns the expected total cost for each customer
        assertEquals(new BigDecimal("150.00"), totalCostByCustomer.get(1L));
        assertEquals(new BigDecimal("0.00"), totalCostByCustomer.get(2L));
    }

    //@Test
    public void testGetTotalCostForProducts() {
        // Create a list of mock transactions for different products
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(createMockTransaction(1L, true, "PRODUCT_001")); // Transaction for PRODUCT_001
        mockTransactions.add(createMockTransaction(2L, true, "PRODUCT_002")); // Transaction for PRODUCT_002
        mockTransactions.add(createMockTransaction(3L, true, "PRODUCT_001")); // Another transaction for PRODUCT_001

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // Call the method under test
        Map<String, BigDecimal> totalCostByProduct = transactionService.getTotalCostForProducts();

        // Verify that the method returns the expected total cost for each product
        assertEquals(new BigDecimal("150.00"), totalCostByProduct.get("PRODUCT_001"));
        assertEquals(new BigDecimal("0.00"), totalCostByProduct.get("PRODUCT_002"));
    }

    //@Test
    public void testGetTransactionsInAustraliaForCustomer() {
        // Create a mock customer and a list of mock transactions for the customer, some from Australia
        Customer mockCustomer = createMockCustomer(1L);
        List<Transaction> mockTransactions = createMockTransactionsForCustomer(mockCustomer);

        // Mock the behavior of customerRepository to return the mock customer
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        // Mock the behavior of transactionRepository to return the list of mock transactions
        Mockito.when(transactionRepository.findByCustomerId(1L)).thenReturn(mockTransactions);

        // Call the method under test
        List<Transaction> transactionsInAustralia = transactionService.getTransactionsInAustraliaForCustomer(1L);

        // Verify that the method returns the expected transactions in Australia
        assertEquals(2, transactionsInAustralia.size());
        assertTrue(transactionsInAustralia.stream().allMatch(t -> t.getCustomerId().equals(1L)));
    }

    private Customer createMockCustomer(Long customerId) {
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(customerId);
        mockCustomer.setLocation("Australia");
        // Set other properties as needed for testing
        return mockCustomer;
    }

    private List<Transaction> createMockTransactionsForCustomer(Customer customer) {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(createMockTransaction(customer.getCustomerId(), true));
        mockTransactions.add(createMockTransaction(customer.getCustomerId(), true));
        mockTransactions.add(createMockTransaction(customer.getCustomerId(), false));
        return mockTransactions;
    }
    
    private Transaction createMockTransaction(Long customerId, String productCode) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setProductCode(productCode);
        return transaction;
    }

    private Transaction createMockTransaction(Long customerId, boolean isCustomerFromAustralia, String productCode) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setProductCode(productCode);
        return transaction;
    }

    private Transaction createMockTransaction(Long customerId, boolean isCustomerFromAustralia) {
        String productCode = "DEFAULT_PRODUCT";
        return createMockTransaction(customerId, isCustomerFromAustralia, productCode);
    }

    private Transaction createMockTransaction(Long customerId) {
        boolean isCustomerFromAustralia = false;
        String productCode = "DEFAULT_PRODUCT";
        return createMockTransaction(customerId, isCustomerFromAustralia, productCode);
    }
    
    //@Test
    public void testAddTransaction() {
        // Create a sample transaction request
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCustomerId(1L);
        transactionRequest.setProductCode("PRODUCT_001");

        // Mock customer and product retrieval
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(1L);
        mockCustomer.setLocation("Australia");

        Product mockProduct = new Product();
        mockProduct.setProductCode("PRODUCT_001");

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        Mockito.when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(mockProduct));

        // Mock validation
        Mockito.doNothing().when(transactionValidator).validate(Mockito.any(), Mockito.any());

        // Mock transaction saving
        Transaction mockTransaction = new Transaction();
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(mockTransaction);

        // Perform the test
        Transaction result = transactionService.addTransaction(transactionRequest);

        // Assertions
        assertNotNull(result);
    }

    //@Test
    public void testGetTransactionsToAustraliaCount1() {
        // Mock transaction data
        List<Transaction> mockTransactions = new ArrayList<>();
        // Add transactions to Australia to the list

        Mockito.when(transactionRepository.findAll()).thenReturn(mockTransactions);

        long count = transactionService.getTransactionsToAustraliaCount();

        assertEquals(0L, count); 
    }

}
