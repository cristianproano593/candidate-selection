package com.example.auth_service.dto;

public class LoginResponse {
    private String token;
    private long expiresIn; // milisegundos
    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
    
    // getters/setters
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
   
    
    
}
