package com.dmm.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.services.UserService;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableMaybeObserver;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etSecurityQuestion, etSecurityAnswer;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UserService
        UserService userService = UserService.getInstance(this);

        // Initialize UI elements
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etSecurityQuestion = findViewById(R.id.etSecurityQuestion);
        etSecurityAnswer = findViewById(R.id.etSecurityAnswer);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Set up Sign Up button click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String securityQuestion = etSecurityQuestion.getText().toString().trim();
                String securityAnswer = etSecurityAnswer.getText().toString().trim();

                // Input validation
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
                MaybeObserver<Boolean> userExists = userService.checkEmailExists(email).subscribeWith(
                        new DisposableMaybeObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean exists) {
                                if (exists) {
                                    Toast.makeText(SignUpActivity.this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                // Save user to the database
                                var sub = userService.addUser(email, name, password, securityQuestion, securityAnswer).subscribeWith(
                                        new DisposableCompletableObserver() {
                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Toast.makeText(SignUpActivity.this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onComplete() {
                                                Toast.makeText(SignUpActivity.this, "Sign up successful! Please log in.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        }
                                );
                            }
                        }
                );
            }
        });
    }
}
