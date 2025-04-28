package br.fecap.pi.uberpass.api;

import br.fecap.pi.uberpass.model.LoginRequest;
import br.fecap.pi.uberpass.model.LoginResponse;
import br.fecap.pi.uberpass.model.ApiResponse;
import br.fecap.pi.uberpass.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/Cadastro")
    Call<ApiResponse> registerUser(@Body User user);

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}