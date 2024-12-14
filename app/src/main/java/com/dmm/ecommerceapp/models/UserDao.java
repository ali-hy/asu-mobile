package com.dmm.ecommerceapp.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

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
