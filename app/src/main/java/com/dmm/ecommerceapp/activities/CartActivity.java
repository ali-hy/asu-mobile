package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.CartItemWithProduct;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.repositories.CartItemRepository;
import com.dmm.ecommerceapp.services.UserService;
import com.dmm.ecommerceapp.viewmodels.CartViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView tvTotalPrice;
    private TextView tvEmptyCart; // Added for the empty cart message
    private Button btnCheckout;
    private Button btnClearCart;
    private CartViewModel cartViewModel;
    private List<CartItemWithProduct> cartItems;
    private UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        userService = UserService.getInstance(this);

        // Initialize Views
        cartItemsContainer = findViewById(R.id.cart_items_container);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvEmptyCart = findViewById(R.id.tv_empty_cart); // Initialize tvEmptyCart
        btnCheckout = findViewById(R.id.btn_checkout);
        btnClearCart = findViewById(R.id.btn_clear_cart);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Observe Cart Items
        cartViewModel
                .getCartItemsByUserId(userService.getCurrentUser().getId())
                .observe(this, items -> {
                    cartItems = items;

                    if (items == null || items.isEmpty()) {
                        tvEmptyCart.setVisibility(View.VISIBLE); // Show empty cart message
                        cartItemsContainer.setVisibility(View.GONE); // Hide cart items container
                        btnCheckout.setEnabled(false); // Disable checkout button
                    } else {
                        tvEmptyCart.setVisibility(View.GONE); // Hide empty cart message
                        cartItemsContainer.setVisibility(View.VISIBLE); // Show cart items container
                        displayCartItems(items); // Populate cart items dynamically
                        btnCheckout.setEnabled(true); // Enable checkout button
                    }
                });

        // Observe Total Price
        cartViewModel.getCartTotal().observe(this, total ->
                tvTotalPrice.setText("Total: $" + String.format("%.2f", total))
        );

        // Checkout Button Action
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty! Add items before checkout.", Toast.LENGTH_SHORT).show();
                return; // Exit if the cart is empty
            }

            double totalPrice = calculateTotalPrice(cartItems);
            String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Order newOrder = new Order();
            newOrder.setTotalAmount(totalPrice);
            newOrder.setOrderDate(orderDate);

            cartViewModel.checkout(newOrder);
            Snackbar.make(findViewById(android.R.id.content), "Order placed successfully!", Snackbar.LENGTH_LONG).show();

            cartViewModel.clearCart(); // Clear the cart after checkout
        });

        // Clear Cart Button Action
        btnClearCart.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear Cart")
                    .setMessage("Are you sure you want to clear your cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        cartViewModel.clearCart();
                        Toast.makeText(this, "Cart cleared successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void getAndDisplayCartItems() {
        CartItemRepository cartItemRepository = new CartItemRepository(getApplication());
        LiveData<List<CartItemWithProduct>> allCartItems = cartItemRepository.getCartItemsByUserID(
                userService.getCurrentUser().getId()
        );
        allCartItems.observe(this, cartItems -> {
            if (cartItems == null || cartItems.isEmpty()) {
                tvEmptyCart.setVisibility(View.VISIBLE); // Show empty cart message
                cartItemsContainer.setVisibility(View.GONE); // Hide cart items container
                btnCheckout.setEnabled(false); // Disable checkout button
            } else {
                tvEmptyCart.setVisibility(View.GONE); // Hide empty cart message
                cartItemsContainer.setVisibility(View.VISIBLE); // Show cart items container
                displayCartItems(cartItems); // Populate cart items dynamically
                btnCheckout.setEnabled(true); // Enable checkout button
            }
        });
    }

    private void displayCartItems(List<CartItemWithProduct> cartItems) {
        cartItemsContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItemWithProduct cartItemWithProduct : cartItems) {
            // Inflate the product item layout
            View cartItemView = inflater.inflate(R.layout.activity_item_cart, cartItemsContainer, false);

            CartItem cartItem = cartItemWithProduct.cartItem;
            Product product = cartItemWithProduct.product;

            TextView tvProductName = cartItemView.findViewById(R.id.tv_product_name);
            tvProductName.setText(product.getName());
            tvProductName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnDecreaseQuantity = cartItemView.findViewById(R.id.btn_decrease_quantity);
            btnDecreaseQuantity.setOnClickListener(v -> cartViewModel.decreaseQuantity(cartItem));

            TextView tvQuantity = cartItemView.findViewById(R.id.tv_quantity);
            tvQuantity.setText("Qty: " + cartItem.getQuantity());
            tvQuantity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnRemove = cartItemView.findViewById(R.id.btn_remove);
            btnRemove.setOnClickListener(v -> cartViewModel.removeCartItem(cartItem));

            Button btnIncreaseQuantity = cartItemView.findViewById(R.id.btn_increase_quantity);
            btnIncreaseQuantity.setOnClickListener(v -> cartViewModel.increaseQuantity(cartItem));

            // Add cart item layout to the container
            cartItemsContainer.addView(cartItemView);
        }
    }

    private double calculateTotalPrice(List<CartItemWithProduct> cartItems) {
        double totalPrice = 0.0;
        for (CartItemWithProduct cartItemWithProduct : cartItems) {
            CartItem cartItem = cartItemWithProduct.cartItem;
            totalPrice += cartItem.getQuantity() * cartItem.getTotalPrice();
        }
        return totalPrice;
    }
}
