package com.example.auth_service.exception;


public class AuthApplicationException extends ApplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthApplicationException(int status, String error, String message) {
        super(status, error, message);
    }
}