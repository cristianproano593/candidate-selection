package com.example.auth_service.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CustomerAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
