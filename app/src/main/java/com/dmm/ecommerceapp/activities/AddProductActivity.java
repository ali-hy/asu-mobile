package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.CategoryRepository;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.utils.CategoryAdapter;

public class AddProductActivity extends AppCompatActivity {

    Spinner sCategory;
    Category selectedCategory;

    private EditText etProductName, etProductDescription, etProductPrice, etProductQuantity;
    private Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        CategoryRepository categoryRepository = new CategoryRepository(getApplication());

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        sCategory = findViewById(R.id.sCategory);

        categoryRepository.getAllCategories().observe(this, categories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            sCategory.setAdapter(categoryAdapter);

            sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCategory = categories.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedCategory = null;
                }
            });
        });


        // Handle button click
        btnAddProduct.setOnClickListener(view -> {
            String name = etProductName.getText().toString().trim();
            String description = etProductDescription.getText().toString().trim();
            String price = etProductPrice.getText().toString().trim();
            String quantity = etProductQuantity.getText().toString().trim();

            if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || selectedCategory == null) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Add product to database
                addProductToDatabase(name, description, Double.parseDouble(price), Integer.parseInt(quantity), selectedCategory.getId());
            }
        });
    }

    private void addProductToDatabase(String name, String description, double price, int quantity, long categoryId) {
        ProductRepository productRepository = new ProductRepository(getApplication());
        Product product = new Product(name, price, description, quantity, categoryId);;

        productRepository.insert(product, () -> {
            Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }, (e) -> {
            Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}