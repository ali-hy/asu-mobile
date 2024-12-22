/*package com.dmm.ecommerceapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.EcommerceDatabase;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.models.CartItem;
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
    public void update(Product product) {
        executorService.execute(() -> productDao.update(product));
    }

    public void delete(Product product) {
        executorService.execute(() -> productDao.delete(product));
    }

    public LiveData<List<Product>> searchProducts(String query) {
        return productDao.searchProducts("%" + query + "%");
    }
}
*/

package com.dmm.ecommerceapp.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dmm.ecommerceapp.data.EcommerceDatabase;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.utils.IFunction;
import com.dmm.ecommerceapp.utils.IFunctionNoParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // In-memory product list
    private final List<Product> inMemoryProducts;
    private final MutableLiveData<List<Product>> inMemoryLiveData = new MutableLiveData<>();

    public ProductRepository(Application application) {
        EcommerceDatabase database = EcommerceDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();

        // Initialize in-memory product list
        inMemoryProducts = new ArrayList<>();
        populateInMemoryProducts();
        inMemoryLiveData.setValue(inMemoryProducts);
    }

    // Add sample products to the in-memory list
    private void populateInMemoryProducts() {
//        inMemoryProducts.add(new Product("In-Memory Product 1", "Description for Product 1", 12.99, "111111", "image_url_1"));
//        inMemoryProducts.add(new Product("In-Memory Product 2", "Description for Product 2", 8.49, "222222", "image_url_2"));
//        inMemoryProducts.add(new Product("In-Memory Product 3", "Description for Product 3", 19.99, "333333", "image_url_3"));
    }

    // Database-related methods
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }


        public void insert(Product product, IFunctionNoParam<Void> onSuccess, IFunctionNoParam<Void> onError) {
            executorService.execute(() -> {
                    try {
                        productDao.insert(product);
                        new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() -> onError.apply());
                    }
                }
            );
        }

    public void update(Product product, IFunctionNoParam<Void> onSuccess, IFunctionNoParam<Void> onError) {
        executorService.execute(() -> {
            try {
                productDao.update(product);
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.apply());
            }
        });
    }

    public LiveData<List<Product>> searchProducts(String query) {
        return productDao.searchProducts("%" + query + "%");
    }

    // In-memory methods
    public LiveData<List<Product>> getInMemoryProducts() {
        return inMemoryLiveData;
    }

    public void addProductToMemory(Product product) {
        inMemoryProducts.add(product);
        inMemoryLiveData.postValue(inMemoryProducts); // Notify observers
    }

    public void removeProductFromMemory(Product product) {
        inMemoryProducts.remove(product);
        inMemoryLiveData.postValue(inMemoryProducts); // Notify observers
    }

    public void deleteProduct(Product product, IFunctionNoParam<Void> onSuccess, IFunctionNoParam<Void> onError) {
        executorService.execute(() -> {
            try {
                productDao.delete(product);
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());;
            } catch (Exception e) {
                productDao.delete(product);
            }
        });
    }
}