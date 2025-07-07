package com.example.auth_service.exception;


public class InvalidCredentialsException extends AuthApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException(String message) {
        super(401, "Invalid Credentials", message);
    }
}