package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmm.ecommerceapp.models.Sales;

import java.util.List;

@Dao
public interface SalesDao {
    @Insert
    void insert(Sales sales);

    @Update
    void update(Sales sales);

    @Delete
    void delete(Sales sales);

    @Query("SELECT * FROM sales_table")
    LiveData<List<Sales>> getAllSales();

    @Query("SELECT * FROM sales_table WHERE productId == :productId")
    LiveData<List<Sales>> searchSalesByProduct(long productId);

    @Query("SELECT * FROM sales_table WHERE userId = :userId")
    LiveData<List<Sales>> searchSalesByUser(long userId);

    @Query("SELECT * FROM sales_table WHERE orderDate LIKE :searchQuery")
    LiveData<List<Sales>> searchSalesByDate(String searchQuery);

    @Query("SELECT * FROM sales_table WHERE id = :productId")
    LiveData<List<Sales>> returnRatingAndFeedbackById(String productId);

    @Query("SELECT * FROM sales_table WHERE productId = :productId")
    LiveData<List<Sales>> returnRatingAndFeedbackByProductId(long productId);

    @Query("SELECT * FROM sales_table WHERE productId = :productId")
    LiveData<List<Sales>> returnListQuantityByProductId(long productId);
}
