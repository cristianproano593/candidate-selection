package com.example.customer_service.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends CustomerApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), "Customer Not Found", message);
    }
}