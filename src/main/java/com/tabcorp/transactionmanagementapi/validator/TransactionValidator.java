package com.tabcorp.transactionmanagementapi.validator;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.ProductNotFoundException;
import com.tabcorp.transactionmanagementapi.errorhandling.TransactionValidationException;
import com.tabcorp.transactionmanagementapi.models.Product;

@Component
public class TransactionValidator {
	private static final Logger logger = Logger.getLogger(TransactionValidator.class);

    public void validate(TransactionRequest transactionRequest, Product product) throws TransactionValidationException {
        validateDate(transactionRequest.getTransactionTime());
        validateCost(transactionRequest.getQuantity(), product);
        // Add additional validation rules as needed
    }

    private void validateDate(LocalDateTime transactionTime) throws TransactionValidationException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (transactionTime != null && transactionTime.isBefore(currentDateTime)) {
            throw new TransactionValidationException("Transaction date cannot be in the past.");
        }
    }


    private void validateCost(int quantity, Product product) {
        // Assuming you have access to product data to check product status and cost
    	String productCode = product.getProductCode();
        if (product == null) {
        	logger.error("Product not found: " + productCode);
            throw new ProductNotFoundException("Product not found: " + productCode);
        }
        
        
        
        if (product.getStatus().equals("Inactive")) {
        	logger.error("Product is inactive: " + productCode);
            throw new ProductNotFoundException("Product is inactive: " + productCode);
        }
        logger.info("Product Cost: " + product.getCost());
        logger.info("Product quantity: " + quantity);

        double totalCost = quantity * product.getCost().doubleValue();
        logger.info("Total cost: " + totalCost);
        if (totalCost > 5000) {
        	logger.error("total Cost is exceeds 5000: " + totalCost);
      	
            throw new TransactionValidationException("Transaction cost exceeds the limit.");
        }
    }
}
