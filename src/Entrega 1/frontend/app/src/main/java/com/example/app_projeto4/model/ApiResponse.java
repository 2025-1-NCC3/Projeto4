package com.example.app_projeto4.model;

public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse() { }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
