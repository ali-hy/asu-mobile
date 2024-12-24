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
import com.dmm.ecommerceapp.models.Sales;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        userService = UserService.getInstance(this);

        // Initialize views
        cartItemsContainer = findViewById(R.id.cart_items_container);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvEmptyCart = findViewById(R.id.tv_empty_cart);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnClearCart = findViewById(R.id.btn_clear_cart);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Observe cart items
        cartViewModel.getCartItemsByUserId(userService.getCurrentUser().getId()).observe(this, items -> {
            cartItems = items;
            if (items == null || items.isEmpty()) {
                showEmptyCart();
            } else {
                showCartItems(items);
            }
        });

        // Set up checkout button
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalPrice = calculateTotalPrice(cartItems);
            String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            for(CartItemWithProduct cartItemWithProduct : cartItems)
            {
                Sales sales = new Sales();
                sales.setTotalAmount(totalPrice);
                sales.setOrderDate(orderDate);
                sales.setUserId(userService.getCurrentUser().getId());
                sales.setQuantity(cartItemWithProduct.cartItem.getQuantity());
                sales.setProductId(cartItemWithProduct.cartItem.getProductId());
                cartViewModel.createNewSale(sales);
            }
            Order newOrder = new Order();
            newOrder.setTotalAmount(totalPrice);
            newOrder.setOrderDate(orderDate);

            cartViewModel.checkout(newOrder);
            Snackbar.make(findViewById(android.R.id.content), "Order placed successfully!", Snackbar.LENGTH_LONG).show();

            cartViewModel.clearCart(); // Clear the cart after checkout
            showEmptyCart();
        });

        // Set up clear cart button
        btnClearCart.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Clear Cart")
                .setMessage("Are you sure you want to clear your cart?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cartViewModel.clearCart();
                    showEmptyCart();
                    Toast.makeText(this, "Cart cleared successfully!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show());
    }

    private void showEmptyCart() {
        tvEmptyCart.setVisibility(View.VISIBLE);
        cartItemsContainer.setVisibility(View.GONE);
        btnCheckout.setEnabled(false);
        tvTotalPrice.setText("Total: $0.00");
    }

    private void showCartItems(List<CartItemWithProduct> items) {
        tvEmptyCart.setVisibility(View.GONE);
        cartItemsContainer.setVisibility(View.VISIBLE);
        btnCheckout.setEnabled(true);

        cartItemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItemWithProduct item : items) {
            View cartItemView = inflater.inflate(R.layout.activity_item_cart, cartItemsContainer, false);

            TextView tvProductName = cartItemView.findViewById(R.id.tv_product_name);
            TextView tvQuantity = cartItemView.findViewById(R.id.tv_quantity);
            Button btnRemove = cartItemView.findViewById(R.id.btn_remove);
            Button btnIncreaseQuantity = cartItemView.findViewById(R.id.btn_increase_quantity);
            Button btnDecreaseQuantity = cartItemView.findViewById(R.id.btn_decrease_quantity);

            Product product = item.product;
            CartItem cartItem = item.cartItem;

            tvProductName.setText(product.getName());
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            btnIncreaseQuantity.setOnClickListener(v -> cartViewModel.increaseQuantity(cartItem));
            btnDecreaseQuantity.setOnClickListener(v -> cartViewModel.decreaseQuantity(cartItem));
            btnRemove.setOnClickListener(v -> cartViewModel.removeCartItem(cartItem));

            cartItemsContainer.addView(cartItemView);
        }

        double totalPrice = calculateTotalPrice(items);
        tvTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    private double calculateTotalPrice(List<CartItemWithProduct> cartItems) {
        double totalPrice = 0.0;
        for (CartItemWithProduct item : cartItems) {
            CartItem cartItem = item.cartItem;
            totalPrice += cartItem.getQuantity() * item.product.getPrice();
        }
        return totalPrice;
    }
}
