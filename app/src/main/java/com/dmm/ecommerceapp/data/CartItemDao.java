package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Delete;

import com.dmm.ecommerceapp.models.CartItem;
import com.dmm.ecommerceapp.models.CartItemWithProduct;

import java.util.List;

@Dao
public interface CartItemDao {
    @Transaction
    @Query("SELECT * FROM cart_item_table WHERE userId = :userId")
    LiveData<List<CartItemWithProduct>> getCartItemByUserID(long userId);

    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_item_table")
    LiveData<List<CartItem>> getAllCartItems();

    @Query("DELETE FROM cart_item_table")
    void deleteAllCartItems();

}

