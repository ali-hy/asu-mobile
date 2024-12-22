package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.db.DatabaseHelper;

public class AddProductActivity extends AppCompatActivity {

    private EditText etProductName, etProductDescription, etProductPrice, etProductQuantity;
    private Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Handle button click
        btnAddProduct.setOnClickListener(view -> {
            String name = etProductName.getText().toString().trim();
            String description = etProductDescription.getText().toString().trim();
            String price = etProductPrice.getText().toString().trim();
            String quantity = etProductQuantity.getText().toString().trim();

            if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Add product to database
                addProductToDatabase(name, description, Double.parseDouble(price), Integer.parseInt(quantity));
            }
        });
    }

    private void addProductToDatabase(String name, String description, double price, int quantity) {
        // Get an instance of DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Insert product into the database
        boolean success = databaseHelper.addProduct(name, description, price, quantity, 1); // Assuming category ID is 1 for now

        if (success) {
            Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
        }
    }

}