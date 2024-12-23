package com.dmm.ecommerceapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmm.ecommerceapp.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories_table")
    public LiveData<List<Category>> getAllCategories();

    @Insert
    public void insert(Category category);

    @Update
    public void update(Category category);

    @Delete
    public void delete(Category category);
}
