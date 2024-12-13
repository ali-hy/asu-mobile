package com.dmm.ecommerceapp.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUserByEmail(String email);

    @Insert
    public boolean insert(User user);

    @Update
    public void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();
}
