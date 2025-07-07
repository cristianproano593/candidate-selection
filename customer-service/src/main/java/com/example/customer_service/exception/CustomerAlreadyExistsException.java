package com.example.customer_service.exception;

import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistsException extends CustomerApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), "Customer Already Exists", message);
    }
}