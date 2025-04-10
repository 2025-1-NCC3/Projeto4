package com.example.app_projeto4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_projeto4.api.ApiClient;
import com.example.app_projeto4.api.AuthService;
import com.example.app_projeto4.model.User;
import com.example.app_projeto4.model.ApiResponse;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cadastro extends AppCompatActivity {
    private EditText editTextFullName, editTextEmailSignUp, editTextPasswordSignUp, editTextConfirmPasswordSignUp, editTextPhone;
    private Button buttonSignUp;
    private TextView textViewLoginLink;
    private AuthService authService;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[!@#$%^&*])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializando componentes
        editTextFullName = findViewById(R.id.DigiteSeuNome);
        editTextEmailSignUp = findViewById(R.id.DigiteSeuEmail);
        editTextPhone = findViewById(R.id.digiteSeuTelefone);
        editTextPasswordSignUp = findViewById(R.id.DigiteSuaSenha);
        editTextConfirmPasswordSignUp = findViewById(R.id.ConfimeSuaSenha);
        buttonSignUp = findViewById(R.id.BotaoLogin);
        textViewLoginLink = findViewById(R.id.TextLogin);


        authService = ApiClient.getClient().create(AuthService.class);

        // Redirecionamento para login
        textViewLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSignUp.setOnClickListener(v -> signUpUser());
    }

    private boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private void signUpUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmailSignUp.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPasswordSignUp.getText().toString().trim();
        String confirmPassword = editTextConfirmPasswordSignUp.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordValid(password)) {
            Toast.makeText(this, "A senha deve ter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais (!@#$%^&*)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User(fullName, email, password, "passenger", phone);

        authService.registerUser(user).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Cadastro.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMsg = "Falha no cadastro";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    }
                    Toast.makeText(Cadastro.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Cadastro.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
