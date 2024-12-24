package com.dmm.ecommerceapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.CartItemWithProduct;
import com.dmm.ecommerceapp.models.Sales;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.repositories.SalesRepository;
import com.dmm.ecommerceapp.services.UserService;

import java.util.List;

public class SalesReportActivity extends AppCompatActivity {
    private LinearLayout saleReportContainer;
    private UserService userService;
    private EditText  etuserId;
    private Button searchUserBtn;
    private SalesRepository salesRepository ;
    private ProductRepository productRepository;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        salesRepository =new SalesRepository(getApplication());
        productRepository =new ProductRepository(getApplication());
        userService = UserService.getInstance(this);
        etuserId=findViewById(R.id.etUserId);
        searchUserBtn=findViewById(R.id.btnGenerateReport);
        saleReportContainer=findViewById(R.id.sales_items_container);

        searchUserBtn.setOnClickListener(view -> {
            if(etuserId==null)
            {
                Toast.makeText(this, "please enter userid", Toast.LENGTH_SHORT).show();

            }
            LiveData<List<Sales>> ldUserSales= returnSalesOfUser(String.valueOf(etuserId));

            List<Sales> listSales= ldUserSales.getValue();
            if (listSales == null) {
                // If the data is null, return 0 or handle appropriately
                Toast.makeText(this, "no sales to show", Toast.LENGTH_SHORT).show();
            }else
            {
                saleReportContainer.setVisibility(View.VISIBLE); // Show cart items container
                displaySalesReport(listSales); // Populate cart items dynamically

            }

        });


    }

    private void displaySalesReport(List<Sales> saleItems)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Sales sales : saleItems) {

            View salesItemView = inflater.inflate(R.layout.item_sales_report, saleReportContainer, false);
            TextView tvSaleItemName=  salesItemView.findViewById(R.id.tvProductName);
            String productName= productRepository.getProductNameById(sales.getProductId());
            tvSaleItemName.setText(productName);
            tvSaleItemName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));


            TextView tvQuantity = salesItemView.findViewById(R.id.tvQuantitySold);
            tvQuantity.setText(sales.getQuantity());
            tvQuantity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvPrice = salesItemView.findViewById(R.id.tvTotalPrice);
            tvPrice.setText((int)sales.getTotalAmount());
            tvPrice.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvDate =salesItemView.findViewById(R.id.tvDateOfSale);
            tvDate.setText(sales.getOrderDate());
            tvDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        }
    }



    public LiveData<List<Sales>> returnSalesOfUser(String userid)
    {
        return salesRepository.searchSalesByUser(Long.parseLong(userid));

    }

}
