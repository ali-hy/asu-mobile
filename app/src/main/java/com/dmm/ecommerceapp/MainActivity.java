package com.dmm.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.activities.CartActivity;
import com.dmm.ecommerceapp.activities.SearchActivity;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.CartItemRepository;
import com.dmm.ecommerceapp.repositories.CategoryRepository;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.services.UserService;
import com.dmm.ecommerceapp.utils.CategoryAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    UserService userService;
    CategoryRepository categoryRepository;
    Spinner sCategory;
    Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService = UserService.getInstance(this);
        sCategory = findViewById(R.id.sCategory);
        categoryRepository = new CategoryRepository(getApplication());

        // Get all categories
        categoryRepository.getAllCategories().observe(this, categories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            sCategory.setAdapter(categoryAdapter);

            sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCategory = categories.get(i);
                    getAndDisplayProducts();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedCategory = null;
                }
            });
        });

        // Check if the user is logged in (e.g., shared preferences or session variable)
        if (!isLoggedIn()) {
            // Redirect to LoginActivity if not logged in
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity to prevent going back to it
            return;
        }

        // Set button actions for navigation
        Button btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });


        // Add functionality for navigating to the search page
        Button btnSearchPage = findViewById(R.id.btnSearchPage);
        btnSearchPage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    private boolean isLoggedIn() {
        return userService.isLoggedIn();
    }

    private void getAndDisplayProducts() {
        // Create a sample list of products
        ProductRepository productRepository = new ProductRepository(this.getApplication());
        LiveData<List<Product>> allProducts;

        if (selectedCategory == null) {
            allProducts = productRepository.getAllProducts();
        } else {
            allProducts = productRepository.getProductsByCategory(selectedCategory.getId());
        }

        allProducts.observe(this, products -> {
            displayProducts(findViewById(R.id.productContainer), products);
        });
    }

    private void clearProductsFromView() {
        LinearLayout productContainer = findViewById(R.id.productContainer);
        productContainer.removeAllViews();
    }

    private void displayProducts(LinearLayout container, List<Product> productList) {
        clearProductsFromView();
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
                CartItemRepository cartItemRepository = new CartItemRepository(getApplication());
                cartItemRepository.insert(
                        new CartItem(
                                userService.getCurrentUser().getId(),
                                product.getId(),
                                1,
                                product.getPrice(),
                                product.getName()
                        ),
                        () -> {
                            Toast.makeText(MainActivity.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                            return null;
                        },
                        () -> {
                            Toast.makeText(MainActivity.this, "Error adding to cart!", Toast.LENGTH_SHORT).show();
                            return null;
                        });
            });

            // Add the product view to the container
            container.addView(productView);
        }
    }
}