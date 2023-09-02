package com.tabcorp.transactionmanagementapi.models;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_code")
    private String productCode;

    private BigDecimal cost;

    private String status;

    // Default constructor
    public Product() {
    }
    
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Product(String productCode, BigDecimal cost, String status) {
		super();
		this.productCode = productCode;
		this.cost = cost;
		this.status = status;
	}

 
}
