package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.ProductRepository;

public class EditProductActivity extends AppCompatActivity {

    private EditText etProductId, etProductName, etProductDescription, etProductPrice, etProductQuantity;
    private Button btnEditProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize views
        etProductId = findViewById(R.id.etProductId);
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnEditProduct = findViewById(R.id.btnEditProduct);

        // Handle button click
        btnEditProduct.setOnClickListener(view -> {
            String id = etProductId.getText().toString().trim();
            String name = etProductName.getText().toString().trim();
            String description = etProductDescription.getText().toString().trim();
            String price = etProductPrice.getText().toString().trim();
            String quantity = etProductQuantity.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                updateProductInDatabase(Integer.parseInt(id), name, description, Double.parseDouble(price), Integer.parseInt(quantity));
            }
        });
    }

    private void updateProductInDatabase(long id, String name, String description, double price, int quantity) {
        ProductRepository productRepository = new ProductRepository(getApplication());

        Product product = new Product(name, price, description);;
        product.setId(id);

        productRepository.update(product, () -> {
            Toast.makeText(this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }, () -> {
            Toast.makeText(this, "Failed to Update Product", Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
