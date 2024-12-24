package com.dmm.ecommerceapp.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.CartItemDao;
import com.dmm.ecommerceapp.db.AppDatabase;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.CartItemWithProduct;
import com.dmm.ecommerceapp.utils.IFunctionNoParam;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final LiveData<List<CartItem>> allCartItems;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Constructor accepts Application
    public CartItemRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();
        allCartItems = cartItemDao.getAllCartItems();
    }

    public LiveData<List<CartItemWithProduct>> getCartItemsByUserID(long userId) {
        return cartItemDao.getCartItemByUserID(userId);
    }

    public LiveData<List<CartItem>> getAllCartItems() {
        return allCartItems;
    }

    public void insert(CartItem cartItem, IFunctionNoParam<Void> onSuccess, IFunctionNoParam<Void> onError) {
        executorService.execute(() -> {
            try {
                cartItemDao.insert(cartItem);
                new Handler(Looper.getMainLooper()).post(onSuccess::apply);

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(onError::apply);
            }
        });
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.update(cartItem));
    }

    public void delete(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.delete(cartItem));
    }

    public void clearCart() {
        executorService.execute(cartItemDao::deleteAllCartItems);
    }
}