package com.tabcorp.transactionmanagementapi.errorhandling;

import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TransactionManagementApiExceptionHandler extends ResponseEntityExceptionHandler{

	
	@ExceptionHandler(value= {IllegalInputException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse errorHandlerCustom(IllegalInputException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("CUSTOM-ERROR-400");
		err.setTime(new java.util.Date());
		return err;
	}

	@ExceptionHandler(value= {ProductNotFoundException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse errorHandlerCustom(ProductNotFoundException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("CUSTOM-ERROR-404");
		err.setTime(new java.util.Date());
		return err;
	}
	
	@ExceptionHandler(value= {CustomerNotFoundException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse errorHandlerCustom(CustomerNotFoundException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("CUSTOM-ERROR-511");
		err.setTime(new java.util.Date());
		return err;
	}
	
	@ExceptionHandler(value= {TransactionValidationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse errorHandlerCustom(TransactionValidationException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("CUSTOM-ERROR-511");
		err.setTime(new java.util.Date());
		return err;
	}
}
