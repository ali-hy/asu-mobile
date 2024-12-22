package com.dmm.ecommerceapp.db;

import android.app.Application;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dmm.ecommerceapp.data.CategoryDao;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.data.SalesDao;
import com.dmm.ecommerceapp.data.UserDao;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.data.CartItemDao;


import androidx.room.Room;

import com.dmm.ecommerceapp.data.OrderDao;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.models.User;

@Database(entities = {User.class, CartItem.class, Order.class, Product.class, Sales.class, Category.class}, version = 7, exportSchema = false)
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
    public abstract UserDao userDao();
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
    public abstract ProductDao productDao();
    public abstract SalesDao salesDao();
    public abstract CategoryDao categoryDao();
}

