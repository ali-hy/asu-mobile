package com.dmm.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.db.DatabaseHelper;
import com.dmm.ecommerceapp.services.UserService;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etSecurityQuestion, etSecurityAnswer;
    private Button btnSignUp;

    private DatabaseHelper databaseHelper;

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
                } else if (userService.checkEmailExists(email)) {
                    Toast.makeText(SignUpActivity.this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
                } else {
                    // Save user to the database
                    boolean success = userService.addUser(email, name, password, securityQuestion, securityAnswer);

                    if (success) {
                        Toast.makeText(SignUpActivity.this, "Sign up successful! Please log in.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
