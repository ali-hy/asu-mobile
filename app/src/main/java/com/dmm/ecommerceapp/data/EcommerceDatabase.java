package com.dmm.ecommerceapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.models.User;

@Database(entities = {User.class, Product.class, CartItem.class, Sales.class}, version = 2, exportSchema = false)
public abstract class EcommerceDatabase extends RoomDatabase {
    private static volatile EcommerceDatabase INSTANCE;

    public abstract ProductDao productDao();
    public abstract CartItemDao cartItemDao();
    public abstract SalesDao salesDao();

    public static EcommerceDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (EcommerceDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EcommerceDatabase.class, "ecommerce_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

