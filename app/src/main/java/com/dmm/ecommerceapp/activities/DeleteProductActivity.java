package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.db.DatabaseHelper;

public class DeleteProductActivity extends AppCompatActivity {

    private EditText etProductId;
    private Button btnDeleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        // Initialize views
        etProductId = findViewById(R.id.etProductId);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        // Handle button click
        btnDeleteProduct.setOnClickListener(view -> {
            String id = etProductId.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter a Product ID", Toast.LENGTH_SHORT).show();
            } else {
                deleteProductFromDatabase(Integer.parseInt(id));
            }
        });
    }

    private void deleteProductFromDatabase(int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        boolean success = databaseHelper.deleteProduct(id);

        if (success) {
            Toast.makeText(this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to Delete Product", Toast.LENGTH_SHORT).show();
        }
    }
}
