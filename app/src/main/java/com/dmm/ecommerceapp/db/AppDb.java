package com.dmm.ecommerceapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dmm.ecommerceapp.models.User;
import com.dmm.ecommerceapp.data.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDb extends RoomDatabase {
    public abstract UserDao userDao();
}
