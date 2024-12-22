package com.dmm.ecommerceapp.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.CategoryDao;
import com.dmm.ecommerceapp.db.AppDatabase;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.utils.IFunction;
import com.dmm.ecommerceapp.utils.IFunctionNoParam;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CategoryRepository {
    CategoryDao categoryDao;
    private final Executor executor;

    private final LiveData<List<Category>> allCategories;

    public CategoryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        executor = Executors.newSingleThreadExecutor();

        allCategories = categoryDao.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category category, IFunctionNoParam<Void> onSuccess, IFunction<Throwable, Void> onError) {
        executor.execute(() -> {
            try {
                categoryDao.insert(category);
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.apply(e));
            }
        });
    }

    public void update(Category category, IFunctionNoParam<Void> onSuccess, IFunction<Throwable, Void> onError) {
        executor.execute(() -> {
            executor.execute(() -> {
                try {
                    categoryDao.update(category);
                    new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> onError.apply(e));
                }
            });
        });
    }

    public void delete(Category category, IFunctionNoParam<Void> onSuccess, IFunction<Throwable, Void> onError) {
        executor.execute(() -> {
            executor.execute(() -> {
                try {
                    categoryDao.delete(category);
                    new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> onError.apply(e));
                }
            });
        });
    }
}
