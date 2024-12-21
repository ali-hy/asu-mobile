package com.dmm.ecommerceapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmm.ecommerceapp.models.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user_table WHERE email = :email")
    Maybe<User> getUserByEmail(String email);

    @Insert
    public Completable insert(User user);

    @Update
    public Completable update(User user);

    @Delete
    Completable delete(User user);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();
}
