package com.tabcorp.transactionmanagementapi.models;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.tabcorp.transactionmanagementapi.errorhandling.ProductNotFoundException;
import com.tabcorp.transactionmanagementapi.repository.ProductRepository;
@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime transactionTime;
    private Long customerId;
    private Integer quantity;
    private String productCode;

    // Default constructor
    public Transaction() {
    }
    
	public Transaction(Long id, LocalDateTime transactionTime, Long customerId, Integer quantity, String productCode) {
		super();
		this.id = id;
		this.transactionTime = transactionTime;
		this.customerId = customerId;
		this.quantity = quantity;
		this.productCode = productCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
    
	public BigDecimal getTransactionCost(ProductRepository productRepository) {
        // Fetch the product price from the repository using the product code
        Optional<Product> optionalProduct = productRepository.findById(productCode);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            BigDecimal cost = BigDecimal.valueOf(quantity).multiply(product.getCost());
            return cost;
        } else {
            // Handle the case where the product is not found
            throw new ProductNotFoundException("Product not found for code: " + productCode);
        }
    }
}