package com.dmm.ecommerceapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.CartItemDao;
import com.dmm.ecommerceapp.data.EcommerceDatabase;
import com.dmm.ecommerceapp.models.CartItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final LiveData<List<CartItem>> allCartItems;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Constructor accepts Application
    public CartItemRepository(Application application) {
        EcommerceDatabase database = EcommerceDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();
        allCartItems = cartItemDao.getAllCartItems();
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public void insert(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.insert(cartItem));
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.update(cartItem));
    }

    public void delete(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.delete(cartItem));
    }

    public void clearCart() {
        executorService.execute(() -> cartItemDao.deleteAllCartItems());
    }
}
