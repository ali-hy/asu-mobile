package com.dmm.ecommerceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.db.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etForgotEmail, etSecurityAnswer, etNewPassword;
    private TextView tvSecurityQuestion;
    private Button btnSubmitEmail, btnResetPassword;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etForgotEmail = findViewById(R.id.etForgotEmail);
        etSecurityAnswer = findViewById(R.id.etSecurityAnswer);
        etNewPassword = findViewById(R.id.etNewPassword);
        tvSecurityQuestion = findViewById(R.id.tvSecurityQuestion);
        btnSubmitEmail = findViewById(R.id.btnSubmitEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        dbHelper = new DatabaseHelper(this);

        btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (!dbHelper.checkEmailExists(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                } else {
                    String securityQuestion = dbHelper.getSecurityQuestion(email);
                    tvSecurityQuestion.setText(securityQuestion);
                    tvSecurityQuestion.setVisibility(View.VISIBLE);
                    etSecurityAnswer.setVisibility(View.VISIBLE);
                    etNewPassword.setVisibility(View.VISIBLE);
                    btnResetPassword.setVisibility(View.VISIBLE);
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getText().toString().trim();
                String securityAnswer = etSecurityAnswer.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();

                if (securityAnswer.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!dbHelper.validateSecurityAnswer(email, securityAnswer)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid security answer!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updatePassword(email, newPassword);
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Close ForgotPasswordActivity
                }
            }
        });
    }
}
