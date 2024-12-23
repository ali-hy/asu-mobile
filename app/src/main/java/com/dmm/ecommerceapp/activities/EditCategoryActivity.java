package com.dmm.ecommerceapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Category;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.CategoryRepository;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.dmm.ecommerceapp.utils.CategoryAdapter;

public class EditCategoryActivity extends AppCompatActivity {

    private Spinner sCategory;
    private EditText etCategoryName;
    private Button btnEditCategory;
    private CategoryRepository categoryRepository;
    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        categoryRepository = new CategoryRepository(getApplication());

        // Initialize views
        sCategory = findViewById(R.id.sCategory);
        etCategoryName = findViewById(R.id.etCategoryName);
        btnEditCategory = findViewById(R.id.btnEditCategory);

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
        btnEditCategory.setOnClickListener(view -> {
            String name = etCategoryName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                selectedCategory.setName(name);
                updateCategoryInDatabase(selectedCategory);
            }
        });
    }

    private void updateCategoryInDatabase(Category category) {
        categoryRepository.update(category, () -> {
            Toast.makeText(this, "category Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }, (e) -> {
            Toast.makeText(this, "Failed to Update Product", Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
