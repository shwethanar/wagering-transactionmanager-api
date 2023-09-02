package com.tabcorp.transactionmanagementapi.errorhandling;
public class JsonDeserializationException extends RuntimeException {
    public JsonDeserializationException(String message) {
        super(message);
    }
}