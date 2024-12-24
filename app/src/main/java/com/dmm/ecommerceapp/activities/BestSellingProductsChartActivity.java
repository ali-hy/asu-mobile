package com.dmm.ecommerceapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipPositionMode;
import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.repositories.SalesRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestSellingProductsChartActivity extends AppCompatActivity {

    private AnyChartView anyChartView;
    private SalesRepository salesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_selling_products_chart);

        // Initialize AnyChartView and SalesRepository
        anyChartView = findViewById(R.id.anyChartView);
        salesRepository = new SalesRepository(getApplication());

        // Load chart data
        loadChartData();
    }

    private void loadChartData() {
        // Get LiveData from repository
        LiveData<List<Sales>> salesLiveData = salesRepository.getAllProducts();

        // Observe changes to sales data
        salesLiveData.observe(this, new Observer<List<Sales>>() {
            @Override
            public void onChanged(List<Sales> salesList) {
                if (salesList == null || salesList.isEmpty()) {
                    Toast.makeText(BestSellingProductsChartActivity.this, "No sales data available", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare the data for the chart
                Map<String, Integer> productSalesMap = new HashMap<>();
                for (Sales sale : salesList) {
                    String productName = sale.getName();
                    int quantity = sale.getQuantity();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        productSalesMap.put(productName, productSalesMap.getOrDefault(productName, 0) + quantity);
                    }
                }

                // Create chart
                setupChart(productSalesMap);
            }
        });
    }

    private void setupChart(Map<String, Integer> productSalesMap) {
        // Prepare data entries
        List<DataEntry> dataEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productSalesMap.entrySet()) {
            dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }

        // Create Cartesian chart
        Cartesian cartesian = AnyChart.column();
        cartesian.data(dataEntries);
        cartesian.title("Best Selling Products by Quantity");
        cartesian.yScale().minimum(0);

        cartesian.tooltip()
                .positionMode(TooltipPositionMode.POINT)
                .anchor(Anchor.CENTER_BOTTOM);

        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Products");
        cartesian.yAxis(0).title("Quantity Sold");

        // Set the chart
        anyChartView.setChart(cartesian);
    }
}
