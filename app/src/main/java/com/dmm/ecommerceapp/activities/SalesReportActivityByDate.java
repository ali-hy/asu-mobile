package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
    private Button  btnGenerateReport;
    private TextView tvTotalSales;

    private SalesRepository salesRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_by_date);
        salesRepository =new SalesRepository(getApplication());
        // Initialize views
        etSelectDate = findViewById(R.id.etSelectDate);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        tvTotalSales = findViewById(R.id.tvTotalSales);

        // Initialize database and list
        SalesRepository salesRepository = new SalesRepository(getApplication());
        salesList = new ArrayList<>();

        btnGenerateReport.setOnClickListener(view -> {
            if(etSelectDate==null)
            {
                Toast.makeText(this,"please enter a date",Toast.LENGTH_SHORT).show();
            }
            LiveData<List<Sales>> ldUserSalesByDate = returnSalesOfUserByDate(etSelectDate.getText().toString().trim());

            ldUserSalesByDate.observe(this, salesList -> {
                if (salesList == null || salesList.isEmpty()) {
                    // If the data is null, return 0 or handle appropriately
                    Toast.makeText(this, "no sales to show", Toast.LENGTH_SHORT).show();
                } else {
                    saleReportContainerByDate.setVisibility(View.VISIBLE); // Show cart items container
                    displaySalesReportByDate(salesList); // Populate cart items dynamically
                }
            });
        });



    }

    private void displaySalesReportByDate(List<Sales> saleItems){
        LayoutInflater inflater =LayoutInflater.from(this);
        for (Sales sales : saleItems) {
            View salesItemView = inflater.inflate(R.layout.item_sales_report_by_date, saleReportContainerByDate, false);
            TextView tvUserId = salesItemView.findViewById(R.id.tvUserName);
            tvUserId.setText(Long.toString(sales.getUserId()));
            tvUserId.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvSaleDetails = salesItemView.findViewById(R.id.tvProductDetails);
            tvSaleDetails.setText(Double.toString(sales.getTotalAmount()));
            tvSaleDetails.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvDate = salesItemView.findViewById(R.id.tvSaleDate);
            tvDate.setText(sales.getOrderDate());
            tvDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            saleReportContainerByDate.addView(salesItemView);
        }

    }
    public LiveData<List<Sales>> returnSalesOfUserByDate(String date)
    {
        return salesRepository.searchSalesByDate(date);
    }


}
