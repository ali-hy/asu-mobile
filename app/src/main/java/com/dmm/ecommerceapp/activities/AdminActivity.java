package com.dmm.ecommerceapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;

public class AdminActivity extends AppCompatActivity {

    private Button btnAddProduct, btnEditProduct, btnDeleteProduct;
    private Button btnAddCategory, btnEditCategory, btnDeleteCategory;
    private Button btnUserTransactions, btnSpecificDateTransactions;
    private Button btnViewFeedback, btnBestSellingProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnEditProduct = findViewById(R.id.btnEditProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnEditCategory = findViewById(R.id.btnEditCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        btnUserTransactions= findViewById(R.id.btnUserTransactions);
        btnSpecificDateTransactions=findViewById(R.id.btnSpecificDateTransactions);
        btnViewFeedback=findViewById(R.id.btnViewFeedback);
        btnBestSellingProducts=findViewById(R.id.btnBestSellingProducts);

        // Set button click listeners
        btnAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnEditProduct.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
            startActivity(intent);
        });

        btnDeleteProduct.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, DeleteProductActivity.class);
            startActivity(intent);
        });

        btnAddCategory.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        btnEditCategory.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, EditCategoryActivity.class);
            startActivity(intent);
        });
//
        btnDeleteCategory.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, DeleteCategoryActivity.class);
            startActivity(intent);
        });

        btnUserTransactions.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, SalesReportActivity.class);
            startActivity(intent);
        });

        btnSpecificDateTransactions.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, SalesReportActivityByDate.class);
            startActivity(intent);
        });

        btnViewFeedback.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ViewFeedbackActivity.class);
            startActivity(intent);
        });

        btnBestSellingProducts.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, BestSellingProductsChartActivity.class);
            startActivity(intent);
        });

        // Optional: Display a welcome message
        Toast.makeText(this, "Welcome to the Admin Panel", Toast.LENGTH_SHORT).show();
    }
}
