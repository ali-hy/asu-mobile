package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.repositories.CategoryRepository;
import com.dmm.ecommerceapp.utils.CategoryAdapter;

public class DeleteCategoryActivity extends AppCompatActivity {

    private Spinner sCategory;
    private Button btnDeleteCategory;
    private CategoryRepository categoryRepository;
    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        categoryRepository = new CategoryRepository(getApplication());

        // Initialize views
        sCategory = findViewById(R.id.sCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);

        // Get all categories
        categoryRepository.getAllCategories().observe(this, categories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            sCategory.setAdapter(categoryAdapter);

            sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCategory = categories.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedCategory = null;
                }
            });
        });

        // Handle button click
        btnDeleteCategory.setOnClickListener(view -> {
            if (selectedCategory == null) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                deleteCategoryInDatabase(selectedCategory);
            }
        });
    }

    private void deleteCategoryInDatabase(Category category) {
        categoryRepository.delete(category, () -> {
            Toast.makeText(this, "Category Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }, (e) -> {
            Toast.makeText(this, "Failed to Deleted Product", Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
