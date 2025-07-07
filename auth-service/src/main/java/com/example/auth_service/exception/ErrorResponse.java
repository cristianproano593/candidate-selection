package com.example.auth_service.exception;

import java.time.Instant;
import java.util.List;



public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> validationErrors;
    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters
    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    // Setters
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
