package com.dmm.ecommerceapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.EcommerceDatabase;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.models.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ProductRepository(Application application) {
        EcommerceDatabase database = EcommerceDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        executorService.execute(() -> productDao.insert(product));
    }

    public LiveData<List<Product>> searchProducts(String query) {
        return productDao.searchProducts("%" + query + "%");
    }
}
