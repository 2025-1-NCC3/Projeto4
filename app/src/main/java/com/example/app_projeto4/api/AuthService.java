package com.example.app_projeto4.api;

import com.example.app_projeto4.model.LoginRequest;
import com.example.app_projeto4.model.LoginResponse;
import com.example.app_projeto4.model.ApiResponse;
import com.example.app_projeto4.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/signup")
    Call<ApiResponse> registerUser(@Body User user);

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
