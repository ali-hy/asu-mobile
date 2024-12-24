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
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.Sales;
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
    private final MutableLiveData<List<Product>> inMemoryLiveData = new MutableLiveData<>();

    public ProductRepository(Application application) {
        EcommerceDatabase database = EcommerceDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
    }


    // Database-related methods
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> getProductsByCategory(long categoryId) {
        return productDao.getByCategory(categoryId);
    }

    public void insert(Product product, IFunctionNoParam<Void> onSuccess, IFunction<Throwable, Void> onError) {
        executorService.execute(() -> {
                    try {
                        productDao.insert(product);
                        new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() -> onError.apply(e));
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

    public void deleteProduct(Product product, IFunctionNoParam<Void> onSuccess, IFunctionNoParam<Void> onError) {
        executorService.execute(() -> {
            try {
                productDao.delete(product);
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.apply());
                ;
            } catch (Exception e) {
                productDao.delete(product);
            }
        });
    }
    public String getProductNameById(long id)
    {productDao.getNameById(id);

        LiveData<List<Product>> product = productDao.getNameById(id);

        List<Product> listProducts= product.getValue();

        if (listProducts == null) {
            // If the data is null, return 0 or handle appropriately
            return "";
        }
        String name = "";
        for (Product ProductItem : listProducts) {
            name = ProductItem.getName();
            break;// Assuming Sales has a getQuantity() method
        }
        return name;
    }
}