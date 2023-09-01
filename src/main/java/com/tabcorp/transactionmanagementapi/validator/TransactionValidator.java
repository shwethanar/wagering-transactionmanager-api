package com.tabcorp.transactionmanagementapi.validator;

import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.models.Product;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {

    @Autowired
    private ProductRepository productRepository;

    public void validate(TransactionRequest transactionRequest) {
        Product product = productRepository.findById(transactionRequest.getProductCode()).orElse(null);
        if (product == null || !"Active".equals(product.getStatus())) {
            throw new IllegalArgumentException("Invalid product");
        }

        double totalCost = transactionRequest.getQuantity() * product.getCost();
        if (totalCost > 5000) {
            throw new IllegalArgumentException("Total cost exceeds 5000");
        }
    }
    
}