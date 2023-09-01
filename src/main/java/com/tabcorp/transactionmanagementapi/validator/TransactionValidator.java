package com.tabcorp.transactionmanagementapi.validator;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.ProductNotFoundException;
import com.tabcorp.transactionmanagementapi.errorhandling.TransactionValidationException;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {

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


    private void validateCost(int quantity, Product product) throws TransactionValidationException {
        // Assuming you have access to product data to check product status and cost
    	String productCode = product.getProductCode();
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + productCode);
        }
        
        if (!product.getStatus().equals("isInActive")) {
            throw new ProductNotFoundException("Product is inactive: " + productCode);
        }

        double totalCost = quantity * product.getCost();
        if (totalCost > 5000) {
            throw new TransactionValidationException("Transaction cost exceeds the limit.");
        }
    }
}
