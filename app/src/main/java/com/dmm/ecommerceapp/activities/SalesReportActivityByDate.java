package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.repositories.SalesRepository;
import com.dmm.ecommerceapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class SalesReportActivityByDate extends AppCompatActivity {
    private LinearLayout saleReportContainerByDate;
    private UserService userService;
    private List<Sales> salesList;
    private EditText etSelectDate;
    private Button btnSelectDate, btnGenerateReport;
    private TextView tvTotalSales;

    private SalesRepository salesRepository;
    private ProductRepository productRepository;
    private SalesReportActivity salesReportActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_by_date);
        salesRepository =new SalesRepository(getApplication());
        productRepository =new ProductRepository(getApplication());
        // Initialize views
        etSelectDate = findViewById(R.id.etSelectDate);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        tvTotalSales = findViewById(R.id.tvTotalSales);

        // Initialize database and list
        SalesRepository salesRepository = new SalesRepository(getApplication());
        salesList = new ArrayList<>();





    }



    private void generateReport() {
        String selectedDate = etSelectDate.getText().toString().trim();

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch sales data using the repository
        salesRepository.searchSalesByDate(selectedDate).observe(this, new Observer<List<Sales>>() {
            @Override
            public void onChanged(List<Sales> sales) {
//                if (sales == null || sales.isEmpty()) {
//                    Toast.makeText(GenerateReportActivity.this, "No sales found for the selected date", Toast.LENGTH_SHORT).show();
//                    salesList.clear();
//                    salesReportActivity.notifyDataSetChanged();
//                    tvTotalSales.setText("Total Sales: $0.00");
//                    return;
//                }

                // Populate sales list
                salesList.clear();
                salesList.addAll(sales);
                double totalSales = 0.0;


                // Update UI
//                salesReportActivity.notifyDataSetChanged();
                tvTotalSales.setText(String.format("Total Sales: $%.2f", totalSales));
            }
        });
    }
}
