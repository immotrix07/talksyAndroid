package com.talksy.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.talksy.android.BuildConfig;
import com.talksy.android.R;
import com.talksy.android.data.model.User;
import com.talksy.android.data.repository.AuthRepository;
import com.talksy.android.ui.main.MainActivity;
import com.talksy.android.utils.TokenManager;

public class LoginActivity extends AppCompatActivity {
    
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnSignup;
    private ProgressBar progressBar;
    private AuthRepository authRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Check if user is already logged in
        if (TokenManager.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        
        initViews();
        initRepository();
        setupClickListeners();
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void initRepository() {
        Log.d("LoginActivity", "Backend URL: " + BuildConfig.BASE_URL);
        Log.d("LoginActivity", "Socket URL: " + BuildConfig.SOCKET_URL);
        authRepository = new AuthRepository(this);
    }
    
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
    
    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        
        if (!isValidEmail(email)) {
            etEmail.setError("Enter a valid email");
            return;
        }
        
        showLoading(true);
        Log.d("LoginActivity", "Attempting login with email: " + email);
        
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    showLoading(false);
                    if (user != null) {
                        // Save user data
                        TokenManager.saveUserId(LoginActivity.this, user.get_id());
                        TokenManager.saveUserData(LoginActivity.this, user.getFullName());
                        
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                Log.e("LoginActivity", "Login failed: " + error);
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnSignup.setEnabled(!show);
    }
}
