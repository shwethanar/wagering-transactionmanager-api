package com.tabcorp.transactionmanagementapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public ResponseEntity<String> addTransaction(@RequestBody byte[] requestData) {
        try {
            TransactionRequest transactionRequest = parseTransactionRequest(requestData);
            String result = transactionService.addTransaction(transactionRequest);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid transaction data");
        }
    }

    private TransactionRequest parseTransactionRequest(byte[] requestData) throws IOException {
        // logic for data is binary or JSON and parse accordingly
         // For JSON: parse using objectMapper.readValue
        return objectMapper.readValue(requestData, TransactionRequest.class);
    }
}
