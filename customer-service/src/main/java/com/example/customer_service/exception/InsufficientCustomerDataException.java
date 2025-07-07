package com.example.customer_service.exception;

import org.springframework.http.HttpStatus;

public class InsufficientCustomerDataException extends CustomerApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientCustomerDataException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), "Insufficient Customer Data", message);
    }
}