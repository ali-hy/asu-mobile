package com.dmm.ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.activities.AdminActivity;
import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.services.UserService;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableMaybeObserver;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin, btnSignUp, btnForgotPassword;

    private UserService userService;
    private SharedPreferences sharedPreferences;

    // Admin credentials
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userService = UserService.getInstance(this);
        sharedPreferences = getSharedPreferences("EcommerceAppPrefs", MODE_PRIVATE);

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe); // Remember Me checkbox
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // Check for previously remembered user
        checkRememberedUser();

        // Handle Login Button Click
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Handle Sign Up Button Click
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Handle Forgot Password Button Click
        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isAdmin(email, password)) {
            // Admin login successful
            Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        } else {
            userService.attemptLogin(email, password,
                    new DisposableMaybeObserver<User>() {
                        @Override
                        public void onSuccess(@NonNull User user) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Remember Me functionality
                            if (cbRememberMe.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password); // Save password
                                editor.putBoolean("rememberMe", true);
                                editor.apply();
                            } else {
                                clearRememberedUser();
                            }

                            // Navigate to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    },
                    // Corrected lambda for Void return type
                    () -> {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        return null; // Explicitly return null for Void
                    }
            );

        }
    }

    private void checkRememberedUser() {
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", ""); // Retrieve password
            etEmail.setText(savedEmail);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }
    }

    private void clearRememberedUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password"); // Clear password
        editor.remove("rememberMe");
        editor.apply();
    }

    // Check if credentials match admin credentials
    private boolean isAdmin(String email, String password) {
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }
}
