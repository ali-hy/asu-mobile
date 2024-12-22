package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.repositories.CategoryRepository;

public class AddCategoryActivity extends AppCompatActivity {

    CategoryRepository categoryRepository;
    EditText etCategoryName;
    Button btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);

        etCategoryName = findViewById(R.id.etCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);


        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String categoryName = etCategoryName.getText().toString().trim();
                    categoryRepository = new CategoryRepository(getApplication());

                    if (categoryName.isEmpty()) {
                        Toast.makeText(
                                AddCategoryActivity.this,
                                "Please enter a valid ecommerce",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    categoryRepository.insert(
                            new Category(categoryName),
                            () -> {
                                Toast.makeText(
                                        AddCategoryActivity.this,
                                        "Category added successfully",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                                return null;
                            },
                            (e) -> {
                                Toast.makeText(AddCategoryActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                                return null;
                            });
                } catch (Exception e) {
                    System.out.println("Lol");
                }
            }
        });
    }
}