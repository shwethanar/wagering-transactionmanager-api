package com.tabcorp.transactionmanagementapi.errorhandling;

import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TransactionManagementApiExceptionHandler extends ResponseEntityExceptionHandler{

	
	@ExceptionHandler(value= {IllegalInputException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
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
		err.setErrorCode("CUSTOM-ERROR-512");
		err.setTime(new java.util.Date());
		return err;
	}
	
	@ExceptionHandler(value= {JsonDeserializationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse errorHandlerCustom(JsonDeserializationException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("CUSTOM-ERROR-513");
		err.setTime(new java.util.Date());
		return err;
	}
	
	/*@ExceptionHandler(value= {NoHandlerFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ErrorResponse errorHandlerCustom(NoHandlerFoundException ex, WebRequest req){
		ErrorResponse err = new ErrorResponse();
		err.setMessage(ex.getMessage());
		err.setErrorCode("The requested endpoint is not found.");
		err.setTime(new java.util.Date());
		return err;
	}*/
	
	 /*  @ExceptionHandler(NoHandlerFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ResponseBody
	    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setErrorCode("NOT_FOUND");
	        errorResponse.setMessage("The requested endpoint is not found.");
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }*/
}
