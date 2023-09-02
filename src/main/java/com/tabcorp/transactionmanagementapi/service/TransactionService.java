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

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class TransactionService {
	
	private static final Logger logger = Logger.getLogger(TransactionService.class);

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    @Autowired
    public TransactionService( CustomerRepository customerRepository,  ProductRepository productRepository,
            TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    public Transaction addTransaction(TransactionRequest transactionRequest) {
       

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

        
        // Save the transaction in the repository
        Transaction savedTransaction = transactionRepository.save(transaction);

        return savedTransaction;
       
    }
    
 
    public long getTransactionsToAustraliaCount() {
        // Fetch all transactions
        List<Transaction> allTransactions = transactionRepository.findAll();

        // Filter transactions sold to customers from Australia
        long australiaTransactionCount = allTransactions.stream()
                .filter(transaction -> isCustomerFromAustralia(transaction.getCustomerId()))
                .count();

        return australiaTransactionCount;
    }

    // Helper method to check if a customer is from Australia
    private boolean isCustomerFromAustralia(Long customerId) {
        // Fetch the customer's information from the repository
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return "Australia".equalsIgnoreCase(customer.getLocation()); 
        }

        return false; // Customer not found or not from Australia
    }

    public BigDecimal getTotalCostByCustomer(Long customerId) {
        // Fetch the customer's transactions from the repository
        List<Transaction> customerTransactions = transactionRepository.findByCustomerId(customerId);
        logger.info("total count of customerTransactions :"+customerTransactions.size());
        
        // Calculate the total cost by summing the costs of all transactions
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Transaction transaction : customerTransactions) {
            BigDecimal transactionCost = transaction.getTransactionCost(productRepository);
            totalCost = totalCost.add(transactionCost);
        }

        return totalCost;
    }

    public BigDecimal getTotalCostByProduct(String productCode) {
        // Fetch the product's transactions from the repository
        List<Transaction> productTransactions = transactionRepository.findByProductCode(productCode);

        logger.info("getTotalCostByProduct::ProductTransactions count :"+productTransactions.size());
        // Calculate the total cost by summing the costs of all transactions
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Transaction transaction : productTransactions) {
            BigDecimal transactionCost = transaction.getTransactionCost(productRepository);
            totalCost = totalCost.add(transactionCost);
        }
        logger.info("getTotalCostByProduct::totalCost :"+totalCost);
        return totalCost;
    }

   /* 
    public Transaction parseBinaryData(byte[] binaryData) throws Exception {
        // Create a byte buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(binaryData);

        // Read the transaction data from the buffer
        int transactionId = byteBuffer.getInt();
        String customerId = readString(byteBuffer);
        String productCode = readString(byteBuffer);
        int quantity = byteBuffer.getInt();

        // Retrieve the customer and product
        // Retrieve customer and product
        Customer customer = customerRepository.findById(Long.parseLong(customerId))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        // Validate the transaction request
        transactionValidator.validate(transactionId, Long.parseLong(customerId), productCode, quantity);

        // Create a transaction object
        Transaction transaction = new Transaction();
        
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setCustomerId(customer.getCustomerId());
        transaction.setProductCode(product.getProductCode());
        transaction.setQuantity(transactionRequest.getQuantity());

        return transaction;
    }

    private String readString(ByteBuffer byteBuffer) {
        // Get the length of the string
        int stringLength = byteBuffer.getInt();

        // Read the string data from the buffer
        byte[] bytes = new byte[stringLength];
        byteBuffer.get(bytes);

        // Convert the bytes to a string
        String string = new String(bytes);

        return string;
    }*/
}