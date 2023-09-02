package com.tabcorp.transactionmanagementapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabcorp.transactionmanagementapi.errorhandling.ErrorResponse;
import com.tabcorp.transactionmanagementapi.errorhandling.IllegalInputException;
import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.TransactionValidationException;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.service.TransactionService;

import org.apache.commons.codec.BinaryDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.BinaryCodec;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Endpoint to accept JSON-encoded transactions
    @PostMapping("/json")
    public ResponseEntity<Transaction> createTransactionFromJson(@RequestBody TransactionRequest transactionRequest) {
        
            Transaction createdTransaction = transactionService.addTransaction(transactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
       
    }
    
    @GetMapping("/")
    public ResponseEntity<ErrorResponse> handleNoFirstName() {
		// To handle the case when firstName is not provided in the URL
        String errorMessage = "Please provide a valid firstName.";
        throw new IllegalInputException(errorMessage);
    }
    
	
	@GetMapping("/report/total-cost-by-customer/{customerId}")
    public ResponseEntity<BigDecimal> getTotalCostByCustomer(@PathVariable Long customerId) {
        BigDecimal totalCost = transactionService.getTotalCostByCustomer(customerId);
        return ResponseEntity.ok(totalCost);
    }

    @GetMapping("/report/total-cost-by-product/{productCode}")
    public ResponseEntity<BigDecimal> getTotalCostByProduct(@PathVariable String productCode) {
        BigDecimal totalCost = transactionService.getTotalCostByProduct(productCode);
        return ResponseEntity.ok(totalCost);
    }

    @GetMapping("/transactions-to-australia-count")
    public ResponseEntity<Long> getTransactionsToAustraliaCount() {
        long transactionCount = transactionService.getTransactionsToAustraliaCount();
        return ResponseEntity.ok(transactionCount);
    }
    
    /*
    // Endpoint to accept binary-encoded transactions
    @PostMapping("/binary")
    public ResponseEntity<Transaction> createTransactionFromBinary(@RequestBody byte[] binaryData) {
        try {
            // Create a transaction object from the binary data
            Transaction transaction = new Transaction();
            // Parse the binary data
            transaction = transactionService.parseBinaryData(binaryData);

            // Validate the transaction
            transactionService.validateTransaction(transaction);

            // Add the transaction to the database
            Transaction createdTransaction = transactionService.addTransaction(transaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (TransactionValidationException e) {
        	
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }*/
  /**/

    // Other endpoints for reporting and additional functionality

    /*// Example endpoint to get all transactions
    @GetMapping("/all")
    public ResponseEntity<List<TransactionRequest>> getAllTransactions() {
        List<TransactionRequest> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }*/
}

