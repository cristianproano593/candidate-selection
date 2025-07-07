package com.example.customer_service.exception;

public class CustomerApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int status;
    private final String error;
    private final String detail;

    public CustomerApplicationException(int status, String error, String detail) {
        super(detail);
        this.status = status;
        this.error = error;
        this.detail = detail;
    }

    // getters para exponer estos campos si los usas en tu handler
    public int getStatus() { return status; }
    public String getError()  { return error; }
    public String getDetail() { return detail; }

}