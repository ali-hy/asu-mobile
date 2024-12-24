package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.repositories.SalesRepository;
import com.dmm.ecommerceapp.services.UserService;

import java.util.List;

public class SalesReportActivity extends AppCompatActivity {
    private LinearLayout saleReportContainer;
    private UserService userService;
    private EditText etuserId;
    private Button searchUserBtn;
    private SalesRepository salesRepository;
    private ProductRepository productRepository;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        salesRepository = new SalesRepository(getApplication());
        productRepository = new ProductRepository(getApplication());
        userService = UserService.getInstance(getApplication());
        etuserId = findViewById(R.id.etUserId);
        searchUserBtn = findViewById(R.id.btnGenerateReport);
        saleReportContainer = findViewById(R.id.sales_items_container);

        searchUserBtn.setOnClickListener(view -> {
            if (etuserId == null) {
                Toast.makeText(this, "please enter userid", Toast.LENGTH_SHORT).show();

            }
            LiveData<List<Sales>> ldUserSales = returnSalesOfUser(etuserId.getText().toString().trim());

            ldUserSales.observe(this, salesList -> {
                if (salesList == null || salesList.isEmpty()) {
                    // If the data is null, return 0 or handle appropriately
                    Toast.makeText(this, "no sales to show", Toast.LENGTH_SHORT).show();
                } else {
                    saleReportContainer.setVisibility(View.VISIBLE); // Show cart items container
                    displaySalesReport(salesList); // Populate cart items dynamically
                }
            });
        });


    }

    private void displaySalesReport(List<Sales> saleItems) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Sales sales : saleItems) {
            View salesItemView = inflater.inflate(R.layout.item_sales_report, saleReportContainer, false);
            TextView tvSaleItemName = salesItemView.findViewById(R.id.tvProductName);
            tvSaleItemName.setText(sales.getName());
            tvSaleItemName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvQuantity = salesItemView.findViewById(R.id.tvQuantitySold);
            tvQuantity.setText(Integer.toString(sales.getQuantity()));
            tvQuantity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvPrice = salesItemView.findViewById(R.id.tvTotalPrice);
            tvPrice.setText(Double.toString(sales.getTotalAmount()));
            tvPrice.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvDate = salesItemView.findViewById(R.id.tvDateOfSale);
            tvDate.setText(sales.getOrderDate());
            tvDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            saleReportContainer.addView(salesItemView);
        }
    }


    public LiveData<List<Sales>> returnSalesOfUser(String userid) {
        return salesRepository.searchSalesByUser(Long.parseLong(userid));
    }
}
