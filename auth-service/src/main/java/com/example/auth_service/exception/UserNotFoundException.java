

package com.example.auth_service.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), "Invalid Credentials", message);
    }
}