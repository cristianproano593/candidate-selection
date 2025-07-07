package com.example.customer_service.config;

public class ApplicationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int status;
    private final String error;

    public ApplicationException(int status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }
    public int getStatus() { return status; }
    public String getError() { return error; }
}