/*package com.tabcorp.transactionmanagementapi.service;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class TransactionServiceTest3 {

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

    @Test
    public void testAddTransaction_Success() {
        TransactionRequest request = new TransactionRequest(); // Create a valid transaction request
        request.setCustomerId(1L);
        request.setProductCode("PRODUCT_001");
        request.setQuantity(2);

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Product product = new Product();
        product.setProductCode("PRODUCT_001");

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(product));
        Mockito.doNothing().when(transactionValidator).validate(request, product);

        String result = transactionService.addTransaction(request);

        // Add assertions based on the expected behavior
    }

    @Test
    public void testAddTransaction_CustomerNotFound() {
        // Test the case where customer is not found
        // Similar to the above test, but set up customerRepository.findById to return Optional.empty()

        String result = transactionService.addTransaction(request);

        // Add assertions based on the expected behavior
    }

    @Test
    public void testAddTransaction_ProductNotFound() {
        // Test the case where the product is not found
        // Similar to the above test, but set up productRepository.findById to return Optional.empty()

        String result = transactionService.addTransaction(request);

        // Add assertions based on the expected behavior
    }

}
*/