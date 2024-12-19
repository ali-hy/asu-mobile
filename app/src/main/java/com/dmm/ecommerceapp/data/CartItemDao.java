package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.dmm.ecommerceapp.models.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_item_table")
    LiveData<List<CartItem>> getAllCartItems();
}

