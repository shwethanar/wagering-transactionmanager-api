package com.tabcorp.transactionmanagementapi.models;
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

    private double cost;

    private String status;

 
}
