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

    @Query("SELECT * FROM sales_table WHERE productId LIKE :searchQuery")
    LiveData<List<Sales>> searchSalesByProduct(String searchQuery);

    @Query("SELECT * FROM sales_table WHERE userId LIKE :searchQuery")
    LiveData<List<Sales>> searchSalesByUser(String searchQuery);

    @Query("SELECT * FROM sales_table WHERE date LIKE :searchQuery")
    LiveData<List<Sales>> searchSalesByDate(String searchQuery);

    @Query("SELECT rating,feedback FROM sales_table WHERE id LIKE :searchQuery")
    LiveData<List<Sales>> returnRatingAndFeedbackById(String searchQuery);

    @Query("SELECT rating,feedback FROM sales_table WHERE productId LIKE :searchQuery")
    LiveData<List<Sales>> returnRatingAndFeedbackByProductId(String searchQuery);

    @Query("SELECT quantity FROM sales_table WHERE productId LIKE :searchQuery")
    LiveData<List<Sales>> returnListQuantityByProductId(String searchQuery);
}
