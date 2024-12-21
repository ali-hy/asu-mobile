package com.dmm.ecommerceapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.data.EcommerceDatabase;
import com.dmm.ecommerceapp.data.ProductDao;
import com.dmm.ecommerceapp.data.SalesDao;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.utils.FeedbackAndRatings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalesRepository {
    private final SalesDao salesDao;
    private final LiveData<List<Sales>> allSales;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SalesRepository(Application application) {
        EcommerceDatabase database = EcommerceDatabase.getInstance(application);
        salesDao = database.salesDao();
        allSales = salesDao.getAllSales();
    }

    public LiveData<List<Sales>> getAllProducts() {
        return allSales;
    }

    public void insert(Sales sales) {
        executorService.execute(() -> salesDao.insert(sales));
    }
    public void update(Sales sales) {
        executorService.execute(() -> salesDao.update(sales));
    }

    public void delete(Sales sales) {
        executorService.execute(() -> salesDao.delete(sales));
    }
    public LiveData<List<Sales>> searchSalesByProduct(String query) {
        return salesDao.searchSalesByProduct("%" + query + "%");
    }
    public LiveData<List<Sales>> searchSalesByUser(String query) {
        return salesDao.searchSalesByUser("%" + query + "%");
    }
    public LiveData<List<Sales>> searchSalesByDate(String query) {
        return salesDao.searchSalesByDate("%" + query + "%");
    }
    public LiveData<List<Sales>> returnRatingAndFeedbackById(String query) {
        return salesDao.returnRatingAndFeedbackById("%" + query + "%");
    }
    public LiveData<List<Sales>> returnRatingAndFeedbackByProductId(String query) {
        return salesDao.returnRatingAndFeedbackByProductId("%" + query + "%");
    }
    public int returnQuantityByProductId(String query) {
        LiveData<List<Sales>> sales = salesDao.returnListQuantityByProductId("%" + query + "%");

        List<Sales> listSales= sales.getValue();
        if (listSales == null) {
            // If the data is null, return 0 or handle appropriately
            return 0;
        }

        int totalQuantity = 0;
        for (Sales saleItem : listSales) {
            totalQuantity += saleItem.getQuantity(); // Assuming Sales has a getQuantity() method
        }

        return totalQuantity;

    }

    public FeedbackAndRatings getFeedbackByDateForUser(String date, long userId) {
        // Get the LiveData object from the DAO
        LiveData<List<Sales>> sales = salesDao.searchSalesByDate(date);

        // Extract the list from LiveData
        List<Sales> listSales = sales.getValue();
        if (listSales == null) {
            // If the data is null, return an empty result
            return new FeedbackAndRatings(new ArrayList<>(), new ArrayList<>());
        }

        // Prepare the feedback and ratings lists
        List<String> feedback = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        // Iterate through the sales and collect feedback and ratings for the specified user
        for (Sales saleItem : listSales) {
            if (saleItem.getUserId() == userId) {
                feedback.add(saleItem.getFeedback());
                ratings.add(saleItem.getRating());
            }
        }

        // Return the encapsulated result
        return new FeedbackAndRatings(feedback, ratings);
    }

    public FeedbackAndRatings getFeedbackByDateForAllUsers(String date)
    {
        // Get the LiveData object from the DAO
        LiveData<List<Sales>> sales = salesDao.searchSalesByDate(date);

        // Extract the list from LiveData
        List<Sales> listSales = sales.getValue();
        if (listSales == null) {
            // If the data is null, return an empty result
            return new FeedbackAndRatings(new ArrayList<>(), new ArrayList<>());
        }

        // Prepare the feedback and ratings lists
        List<String> feedback = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        // Iterate through the sales and collect feedback and ratings for the specified user
        for (Sales saleItem : listSales) {

                feedback.add(saleItem.getFeedback());
                ratings.add(saleItem.getRating());

        }

        // Return the encapsulated result
        return new FeedbackAndRatings(feedback, ratings);
    }
}
