package com.tabcorp.transactionmanagementapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabcorp.transactionmanagementapi.errorhandling.ErrorResponse;
import com.tabcorp.transactionmanagementapi.errorhandling.IllegalInputException;
import com.tabcorp.transactionmanagementapi.errorhandling.JsonDeserializationException;
import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.TransactionValidationException;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.service.TransactionService;
import com.tabcorp.transactionmanagementapi.validator.TransactionValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.codec.BinaryDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private static final Logger logger = Logger.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public TransactionController(TransactionService transactionService, ObjectMapper objectMapper) {
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Create transaction from JSON", description = "This endpoint accepts JSON-encoded transactions, parses them, and creates a new transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/json")
    public ResponseEntity<Transaction> createTransactionFromJson(@RequestBody String jsonRequest) throws JsonProcessingException {
    logger.info("Received JSON request: " + jsonRequest);

    try {	
                // Attempt to parse the JSON request
                TransactionRequest request = objectMapper.readValue(jsonRequest, TransactionRequest.class);

                Transaction createdTransaction = transactionService.addTransaction(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
            } catch (JsonProcessingException e) {
                logger.error("JSON parsing error: " + e.getMessage()); 

                // Handle the JSON parsing exception here
                throw new JsonDeserializationException("Invalid JSON request: " + e.getMessage());
            }
    }
    
    @GetMapping("/")
    public ResponseEntity<ErrorResponse> handleNoMapping() {
		// To handle the case when not provided in the URL
        String errorMessage = "Please provide a URL";
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

    @GetMapping("/report/transactions-to-australia-count")
    public ResponseEntity<Long> getTransactionsToAustraliaCount() {
        long transactionCount = transactionService.getTransactionsToAustraliaCount();
        return ResponseEntity.ok(transactionCount);
    }
    
    @GetMapping("/report/total-cost-per-customer")
    public ResponseEntity<Map<Long, BigDecimal>> getTotalCostForCustomers() {
        Map<Long, BigDecimal> totalCostByCustomer = transactionService.getTotalCostForCustomers();
        return ResponseEntity.ok(totalCostByCustomer);
    }

    @GetMapping("/report/total-cost-per-product")
    public ResponseEntity<Map<String, BigDecimal>> getTotalCostForProducts() {
        Map<String, BigDecimal> totalCostByProduct = transactionService.getTotalCostForProducts();
        return ResponseEntity.ok(totalCostByProduct);
    }

    @Operation(summary = "Get transactions in Australia for a customer", description = "This endpoint retrieves transactions that occurred in Australia for a specific customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/report/transactions-in-australia/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsInAustraliaForCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
        
        List<Transaction> transactionsInAustralia = transactionService.getTransactionsInAustraliaForCustomer(customerId);
        return ResponseEntity.ok(transactionsInAustralia);
    }
    
    @GetMapping("/allTtransactions")
    public Page<Transaction> getAllTransactions(Pageable pageable) {
    	Page<Transaction> allTP= transactionService.getAllTransactions(pageable);
        return allTP;
    }
    
    
    // Endpoint to accept binary-encoded transactions
    @PostMapping("/binary")
    public ResponseEntity<Object> createTransactionFromBinary(@RequestBody byte[] binaryData) {
        try {
            // Create a transaction object from the binary data
            Transaction transaction = new Transaction();
            // Parse the binary data
            transaction = transactionService.parseBinaryData(binaryData);
            // Add the transaction to the database
            //Transaction createdTransaction = transactionService.addTransaction(transaction);

            Object createdTransaction = null;
			return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (TransactionValidationException e) {
        	
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
  /**/


}

