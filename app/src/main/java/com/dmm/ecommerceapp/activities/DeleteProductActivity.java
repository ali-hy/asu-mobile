package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.utils.ProductAdapter;

public class DeleteProductActivity extends AppCompatActivity {

    private Spinner etProductId;
    private Button btnDeleteProduct;
    private ProductRepository productRepository;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        // Initialize views
        etProductId = findViewById(R.id.productIdIn);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        productRepository = new ProductRepository(getApplication());

        productRepository.getAllProducts().observe(this, products -> {
            ProductAdapter adapter = new ProductAdapter(this, products);
            etProductId.setAdapter(adapter);

            etProductId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Get the selected item
                    selectedProduct = products.get(position);
                    // Handle the selected item
                    Toast.makeText(DeleteProductActivity.this, "Selected with id: " + selectedProduct, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    selectedProduct = null;
                }
            });
        });


        // Handle button click
        btnDeleteProduct.setOnClickListener(view -> {
            if (selectedProduct == null) {
                Toast.makeText(this, "Please enter a Product ID", Toast.LENGTH_SHORT).show();
            } else {
                deleteProductFromDatabase(selectedProduct);
            }
        });
    }

    private void deleteProductFromDatabase(Product product) {
        if (productRepository == null)
            productRepository = new ProductRepository(getApplication());

        productRepository.deleteProduct(
                selectedProduct,
                () -> {
                    Toast.makeText(this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    return null;
                },
                () -> {
                    Toast.makeText(this, "Failed to Delete Product", Toast.LENGTH_SHORT).show();
                    return null;
                }
        );
    }
}
