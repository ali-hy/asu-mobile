package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dmm.ecommerceapp.models.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM product_table")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product_table WHERE name LIKE :searchQuery")
    LiveData<List<Product>> searchProducts(String searchQuery);
}
