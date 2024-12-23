package com.dmm.ecommerceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.services.UserService;

import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.observers.DisposableMaybeObserver;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etForgotEmail, etSecurityAnswer, etNewPassword;
    private TextView tvSecurityQuestion;
    private Button btnSubmitEmail, btnResetPassword;

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

        UserService userService = UserService.getInstance(this);

        btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Subscribe to the Single returned by userService.getUserByEmail(email)
                MaybeObserver<User> sub = userService.getUserByEmail(email)
                        .subscribeWith(new DisposableMaybeObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                // Handle the user object when successfully retrieved
                                if (user == null) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
                                } else {
                                    String securityQuestion = user.getSecurityQuestion();
                                    tvSecurityQuestion.setText(securityQuestion);
                                    tvSecurityQuestion.setVisibility(View.VISIBLE);
                                    etSecurityAnswer.setVisibility(View.VISIBLE);
                                    etNewPassword.setVisibility(View.VISIBLE);
                                    btnResetPassword.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                // Handle any error that occurred during the network call
                                Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
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
                }

                MaybeObserver<Boolean> sub = userService.validateSecurityAnswer(email, securityAnswer).subscribeWith(
                        new DisposableMaybeObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean isValid) {
                                if (!isValid) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Invalid security answer!", Toast.LENGTH_SHORT).show();
                                } else {
                                    userService.updatePassword(email, newPassword,() -> {
                                        Toast.makeText(ForgotPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                                        finish(); // Close ForgotPasswordActivity
                                        return null;
                                    } , e -> {
                                        Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return null;
                                    });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );
            }
        });
    }
}
