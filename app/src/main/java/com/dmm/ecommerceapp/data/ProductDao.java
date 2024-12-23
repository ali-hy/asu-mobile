package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmm.ecommerceapp.models.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product_table WHERE name LIKE :searchQuery ORDER BY name ASC")
    LiveData<List<Product>> searchProducts(String searchQuery);

    @Query("SELECT * FROM product_table WHERE categoryId = :categoryId ORDER BY name ASC")
    LiveData<List<Product>> getByCategory(long categoryId);
}
