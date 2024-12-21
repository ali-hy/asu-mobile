package com.dmm.ecommerceapp.db;

import android.content.Context;
import androidx.room.Room;

import com.dmm.ecommerceapp.data.UserDao;

public class DbClient {

    public static DbClient instance;
    private final AppDatabase appDatabase;

    private DbClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app_db").build();
    }

    public static DbClient getInstance(Context context) {
        if (instance == null) {
            instance = new DbClient(context);
        }

        return instance;
    }

    public UserDao userDao(){
        return appDatabase.userDao();
    }
}
