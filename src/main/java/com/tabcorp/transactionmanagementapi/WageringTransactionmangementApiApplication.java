package com.tabcorp.transactionmanagementapi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
@EntityScan(basePackages = "com.tabcorp.transactionmanagementapi.models")
@ComponentScan(basePackages = "com.tabcorp.transactionmanagementapi")
@SpringBootApplication
public class WageringTransactionmangementApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WageringTransactionmangementApiApplication.class, args);
    }
}
