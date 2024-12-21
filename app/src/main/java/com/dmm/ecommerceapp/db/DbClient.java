package com.dmm.ecommerceapp.db;

import android.content.Context;
import androidx.room.Room;

import com.dmm.ecommerceapp.data.UserDao;

import java.lang.ref.WeakReference;

public class DbClient {

    public static DbClient instance;
    private WeakReference<Context> context;
    private final AppDb appDatabase;

    public DbClient(Context context) {
        this.context = new WeakReference<>(context.getApplicationContext());
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDb.class, "app_db").build();
    }

    public UserDao userDao(){
        return appDatabase.userDao();
    }
}
