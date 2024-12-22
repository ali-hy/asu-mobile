package com.dmm.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.activities.CartActivity;
import com.dmm.ecommerceapp.activities.SearchActivity;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.services.UserService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService = UserService.getInstance(this);

        // Check if the user is logged in (e.g., shared preferences or session variable)
        if (!isLoggedIn()) {
            // Redirect to LoginActivity if not logged in
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity to prevent going back to it
            return;
        }

        // Set button actions for navigation
        findViewById(R.id.btnEbooks).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EbooksActivity.class)));
        findViewById(R.id.btnCourses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        findViewById(R.id.btnLicenses).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LicensesActivity.class)));
        findViewById(R.id.btnGiftCards).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GiftCardsActivity.class)));

        // Add functionality for navigating to the search page
        Button btnSearchPage = findViewById(R.id.btnSearchPage);
        btnSearchPage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Add products dynamically
        LinearLayout productContainer = findViewById(R.id.productContainer);

        getAndDisplayProducts();
    }

    private boolean isLoggedIn() {
        return userService.isLoggedIn();
    }

    private void getAndDisplayProducts() {
        // Create a sample list of products
        ProductRepository productRepository = new ProductRepository(this.getApplication());
        LiveData<List<Product>> allProducts = productRepository.getAllProducts();

        allProducts.observe(this, products -> {
            if (products == null || products.isEmpty()) {
                productRepository.insert(new Product("In-Memory Product 1", "Description for Product 1", 12.99, "111111", "image_url_1"));
            }

            clearProductsFromView();
            displayProducts(findViewById(R.id.productContainer), products);
        });
    }

    private void clearProductsFromView() {
        LinearLayout productContainer = findViewById(R.id.productContainer);
        productContainer.removeAllViews();
    }

    private void displayProducts(LinearLayout container, List<Product> productList) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Product product : productList) {
            // Inflate the product item layout
            View productView = inflater.inflate(R.layout.activity_item_product, container, false);

            // Set product details
            TextView productName = productView.findViewById(R.id.tv_product_name);
            TextView productPrice = productView.findViewById(R.id.tv_product_price);
            TextView productDescription = productView.findViewById(R.id.tv_product_description);
            Button addToCartButton = productView.findViewById(R.id.btn_add_to_cart);

            productName.setText(product.getName());
            productPrice.setText(String.format("$%.2f", product.getPrice()));
            productDescription.setText(product.getDescription());

            // Handle Add to Cart button click
            addToCartButton.setOnClickListener(v -> {
                // Add product to cart (placeholder logic)
                Toast.makeText(MainActivity.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            });

            // Add the product view to the container
            container.addView(productView);
        }
    }
}