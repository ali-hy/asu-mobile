package com.dmm.ecommerceapp.db;

import android.content.Context;
import androidx.room.Room;

import com.dmm.ecommerceapp.data.UserDao;

import java.lang.ref.WeakReference;

public class DbClient {

    public static DbClient instance;
    private WeakReference<Context> context;
    private final AppDb appDatabase;

    private DbClient(Context context) {
        this.context = new WeakReference<>(context);
        appDatabase = Room.databaseBuilder(context, AppDb.class, "app_db").build();
    }

    public static DbClient getInstance(Context context) {
        if (instance == null) {
            instance = new DbClient(context);
        }

        instance.context = new WeakReference<>(context);
        return instance;
    }

    public UserDao userDao(){
        return appDatabase.userDao();
    }
}
