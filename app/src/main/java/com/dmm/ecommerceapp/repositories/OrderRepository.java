package com.dmm.ecommerceapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.OrderDao;
import com.dmm.ecommerceapp.db.AppDatabase;
import com.dmm.ecommerceapp.models.Order;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDao orderDao;
    private final Executor executor;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDao = database.orderDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertOrder(Order order) {
        executor.execute(() -> orderDao.insertOrder(order));
    }

    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }
}

