package com.dmm.ecommerceapp.db;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dmm.ecommerceapp.data.CategoryDao;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.data.SalesDao;
import com.dmm.ecommerceapp.data.UserDao;
import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Order;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.models.User;

@Database(entities = {User.class, CartItem.class, Order.class, Product.class, Sales.class, Category.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    // Migration from version 6 to 7 for adding the "dob" column
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add the "dob" column to the "users" table
            database.execSQL("ALTER TABLE user_table ADD COLUMN dob TEXT");
        }
    };

    // Migration from version 5 to 6 (if applicable)
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER DEFAULT 0 NOT NULL");
        }
    };

    // Singleton instance getter
    public static AppDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    application.getApplicationContext(),
                                    AppDatabase.class,
                                    "ecommerce_database"
                            )
                            .addMigrations(MIGRATION_5_6, MIGRATION_6_7) // Add both migrations
                            .build();
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
