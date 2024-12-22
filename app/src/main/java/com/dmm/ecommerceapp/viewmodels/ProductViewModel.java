package com.dmm.ecommerceapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final LiveData<List<Product>> allProducts;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> searchProducts(String query) {
        return repository.searchProducts(query);
    }

    public void insert(Product product) {
//        repository.insert(product);
    }
}
