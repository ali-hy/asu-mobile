package com.dmm.ecommerceapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productspage); // Replace with your admin XML file name

        // Button to switch to User Activity
        Button refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, UserActivity.class);
            startActivity(intent);
        });
    }
}
