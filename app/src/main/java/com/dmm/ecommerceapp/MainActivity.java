package com.dmm.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.activities.CartActivity;
import com.dmm.ecommerceapp.services.UserService;

public class MainActivity extends AppCompatActivity {
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        userService = UserService.getInstance(this);
//
//        if (userService == null) {
//            System.out.println("UserService is null");
//        }

        // Check if the user is logged in (e.g., shared preferences or session variable)
        if (!isLoggedIn()) {
            // Redirect to LoginActivity if not logged in
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity to prevent going back to it
            return;
        }

        findViewById(R.id.btnEbooks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EbooksActivity.class));
            }
        });

        findViewById(R.id.btnCourses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        findViewById(R.id.btnLicenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LicensesActivity.class));
            }
        });

        findViewById(R.id.btnGiftCards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GiftCardsActivity.class));
            }
        });
    }

    private boolean isLoggedIn() {
//        if (userService != null) {
//            return userService.isLoggedIn();
//        } else {
            return true;
//        }
    }
}
