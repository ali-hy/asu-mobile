package com.dmm.ecommerceapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnEditProduct = findViewById(R.id.btnEditProduct);
        Button btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

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
//
//        btnAddCategory.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class);
//            startActivity(intent);
//        });
//
//        btnEditCategory.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, EditCategoryActivity.class);
//            startActivity(intent);
//        });
//
//        btnDeleteCategory.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, DeleteCategoryActivity.class);
//            startActivity(intent);
//        });
//
//        btnUserTransactions.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, UserTransactionsReportActivity.class);
//            startActivity(intent);
//        });
//
//        btnSpecificDateTransactions.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, DateTransactionsReportActivity.class);
//            startActivity(intent);
//        });
//
//        btnViewFeedback.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, ViewFeedbackActivity.class);
//            startActivity(intent);
//        });
//
//        btnBestSellingProducts.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminActivity.this, BestSellingProductsChartActivity.class);
//            startActivity(intent);
//        });
//
//        // Optional: Display a welcome message
//        Toast.makeText(this, "Welcome to the Admin Panel", Toast.LENGTH_SHORT).show();
    }
}
