package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.viewmodels.CartViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartViewModel cartViewModel;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Views
        cartItemsContainer = findViewById(R.id.cart_items_container);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Observe Cart Items
        cartViewModel.getCartItems().observe(this, items -> {
            cartItems = items;
            populateCartItems(items);
        });

        // Observe Total Price
        cartViewModel.getCartTotal().observe(this, total ->
                tvTotalPrice.setText("Total: $" + String.format("%.2f", total))
        );

        // Checkout Button Action
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                double totalPrice = calculateTotalPrice(cartItems);
                String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Order newOrder = new Order();
                newOrder.setTotalAmount(totalPrice);
                newOrder.setOrderDate(orderDate);

                cartViewModel.checkout(newOrder);
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                cartViewModel.clearCart(); // Clear the cart after checkout
            }
        });
    }

    private void populateCartItems(List<CartItem> cartItems) {
        cartItemsContainer.removeAllViews();

        for (CartItem cartItem : cartItems) {
            // Create a dynamic layout for each cart item
            LinearLayout cartItemLayout = new LinearLayout(this);
            cartItemLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvProductName = new TextView(this);
            tvProductName.setText(cartItem.getProductName());
            tvProductName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvQuantity = new TextView(this);
            tvQuantity.setText("Qty: " + cartItem.getQuantity());
            tvQuantity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Button btnRemove = new Button(this);
            btnRemove.setText("Remove");
            btnRemove.setOnClickListener(v -> cartViewModel.removeCartItem(cartItem));

            // Add views to cart item layout
            cartItemLayout.addView(tvProductName);
            cartItemLayout.addView(tvQuantity);
            cartItemLayout.addView(btnRemove);

            // Add cart item layout to the container
            cartItemsContainer.addView(cartItemLayout);
        }
    }

    private double calculateTotalPrice(List<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getQuantity() * cartItem.getTotalPrice();
        }
        return totalPrice;
    }
}
