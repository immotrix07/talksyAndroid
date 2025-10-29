package com.talksy.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.talksy.android.data.models.LoginResponse;
import com.talksy.android.data.models.User;
import com.talksy.android.data.network.ApiClient;
import com.talksy.android.data.network.ApiClient.ApiService;
import com.talksy.android.databinding.ActivityLoginBinding;
import com.talksy.android.ui.chat.ChatListActivity;
import com.talksy.android.utils.TokenManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getClient(this).create(ApiService.class);
        tokenManager = new TokenManager(this);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        binding.textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        apiService.login(user).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(retrofit2.Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        tokenManager.saveToken(loginResponse.getToken());
                        tokenManager.saveUserInfo(
                                loginResponse.getUser().getId(),
                                loginResponse.getUser().getFullName(),
                                loginResponse.getUser().getEmail()
                        );
                        String message = loginResponse.getMessage();
                        if (message == null || message.isEmpty()) {
                            message = "Login successful!";
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, ChatListActivity.class));
                        finish();
                    } else {
                        String errorMessage = loginResponse.getMessage();
                        if (errorMessage == null || errorMessage.isEmpty()) {
                            errorMessage = "Login failed. Please check your credentials.";
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = response.message();
                    if (errorMessage == null || errorMessage.isEmpty()) {
                        errorMessage = "Login failed. Server error.";
                    }
                    Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
