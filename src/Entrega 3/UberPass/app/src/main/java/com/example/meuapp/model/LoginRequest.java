package com.example.meuapp.model;

public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.senha = password;
    }
}
