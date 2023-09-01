package com.tabcorp.transactionmanagementapi.errorhandling;

public class IllegalInputException extends RuntimeException {
    public IllegalInputException(String message) {
        super(message);
    }
}