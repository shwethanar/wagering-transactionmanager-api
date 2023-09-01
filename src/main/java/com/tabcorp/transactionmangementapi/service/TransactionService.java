package com.tabcorp.transactionmangementapi.service;

import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmangementapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction processTransaction(Transaction transaction) {
        // add a logic to validate data, retrieve customer and product details, and save transaction
        return transactionRepository.save(transaction);
    }

}
