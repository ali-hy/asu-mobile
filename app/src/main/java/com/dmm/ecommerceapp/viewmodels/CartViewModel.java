package com.dmm.ecommerceapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.repositories.CartItemRepository;
import com.dmm.ecommerceapp.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private final CartItemRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MutableLiveData<List<CartItem>> cartItemsLiveData;
    private final MutableLiveData<Double> cartTotalLiveData;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartItemRepository(application);
        orderRepository = new OrderRepository(application);
        cartItemsLiveData = new MutableLiveData<>();
        cartTotalLiveData = new MutableLiveData<>(0.0);

        // Load initial data
        loadCartItems();
    }

    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public LiveData<Double> getCartTotalLiveData() {
        return cartTotalLiveData;
    }

    public void addCartItem(CartItem cartItem) {
        cartRepository.insert(cartItem);
        loadCartItems();
    }

    public void removeCartItem(CartItem cartItem) {
        cartRepository.delete(cartItem);
        loadCartItems();
    }

    public void updateCartItem(CartItem cartItem) {
        cartRepository.update(cartItem);
        loadCartItems();
    }

    private void loadCartItems() {
        // Get the cart items from the repository
        LiveData<List<CartItem>> cartItemsLive = cartRepository.getAllCartItems();
        cartItemsLive.observeForever(cartItems -> {
            cartItemsLiveData.setValue(cartItems);

            // Calculate cart total
            calculateCartTotal(cartItems);
        });
    }

    private void calculateCartTotal(List<CartItem> cartItems) {
        double total = 0.0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getTotalPrice(); // Ensure `getTotalPrice()` is correct in `CartItem`
            }
        }
        cartTotalLiveData.setValue(total);
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItemsLiveData;
    }

    public LiveData<Double> getCartTotal() {
        return cartTotalLiveData;
    }

    public void clearCart() {
        cartRepository.clearCart(); // Call repository to delete all cart items
        cartItemsLiveData.setValue(new ArrayList<>()); // Clear current LiveData
        calculateCartTotal(new ArrayList<>()); // Reset total price
    }

    public void checkout(Order newOrder) {
        // Insert the new order into the database
        orderRepository.insertOrder(newOrder);

        // Clear the cart after checkout
        clearCart();
    }


}
