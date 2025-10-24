package com.talksy.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.talksy.android.R;
import com.talksy.android.data.model.User;
import com.talksy.android.data.repository.AuthRepository;
import com.talksy.android.ui.main.MainActivity;
import com.talksy.android.utils.TokenManager;

public class SignupActivity extends AppCompatActivity {
    
    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView btnLogin;
    private ProgressBar progressBar;
    private AuthRepository authRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        initViews();
        initRepository();
        setupClickListeners();
    }
    
    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void initRepository() {
        authRepository = new AuthRepository(this);
    }
    
    private void setupClickListeners() {
        btnSignup.setOnClickListener(v -> attemptSignup());
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
    
    private void attemptSignup() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required");
            return;
        }
        
        if (!isValidEmail(email)) {
            etEmail.setError("Enter a valid email");
            return;
        }
        
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }
        
        showLoading(true);
        
        authRepository.signup(fullName, email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    showLoading(false);
                    if (user != null) {
                        // Save user data
                        TokenManager.saveUserId(SignupActivity.this, user.get_id());
                        TokenManager.saveUserData(SignupActivity.this, user.getFullName());
                        
                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(SignupActivity.this, "Signup failed: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSignup.setEnabled(!show);
        btnLogin.setEnabled(!show);
    }
}
