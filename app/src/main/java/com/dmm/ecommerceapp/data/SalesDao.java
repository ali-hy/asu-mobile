package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.dmm.ecommerceapp.models.Sales;

import java.util.List;

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

    @Query("SELECT * FROM sales_table WHERE userId == :searchQuery")
    LiveData<List<Sales>> searchSalesByUser(long searchQuery);

    @Query("SELECT * FROM sales_table WHERE date LIKE :searchQuery")
    LiveData<List<Sales>> searchSalesByDate(String searchQuery);

    @Query("SELECT rating,feedback FROM sales_table WHERE id == :productId")
    LiveData<List<Sales>> returnRatingAndFeedbackById(String productId);

    @Query("SELECT rating,feedback FROM sales_table WHERE productId == :productId")
    LiveData<List<Sales>> returnRatingAndFeedbackByProductId(long productId);

    @Query("SELECT quantity FROM sales_table WHERE productId == :productId")
    LiveData<List<Sales>> returnListQuantityByProductId(long productId);
}
