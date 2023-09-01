package com.tabcorp.transactionmangementapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmangementapi.service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.processTransaction(transaction);
        return ResponseEntity.ok("Transaction created with ID: " + savedTransaction.getId());
    }
}
