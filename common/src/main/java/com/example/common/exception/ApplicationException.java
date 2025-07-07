package com.example.common.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int status;
    private final String error;
    private final String detail;

    public ApplicationException(int status, String error, String detail) {
        super(detail);
        this.status = status;
        this.error = error;
        this.detail = detail;
    }

    public int getStatus() { return status; }
    public String getError()  { return error; }
    public String getDetail() { return detail; }

}