package com.dmm.ecommerceapp.db;

import android.content.Context;
import androidx.room.Room;

import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.models.UserDao;

import java.lang.ref.WeakReference;
import java.util.List;

public class DbClient {

    public static DbClient instance;
    private WeakReference<Context> context;
    private final AppDb appDatabase;

    private DbClient(Context context) {
        this.context = new WeakReference<>(context.getApplicationContext());
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDb.class, "app_db").build();
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
