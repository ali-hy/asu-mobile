package com.dmm.ecommerceapp.db;

import android.app.Application;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.data.CartItemDao;


import androidx.room.Room;

import com.dmm.ecommerceapp.data.OrderDao;

@Database(entities = {CartItem.class, Order.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    // Singleton instance getter
    public static AppDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            application.getApplicationContext(),
                            AppDatabase.class,
                            "ecommerce_database"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

    // Abstract DAO methods
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
}

