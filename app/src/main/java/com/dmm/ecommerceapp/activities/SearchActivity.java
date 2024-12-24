package com.dmm.ecommerceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Product;
import com.dmm.ecommerceapp.repositories.ProductRepository;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 1;

    private EditText searchInput;
    private Button searchButton, voiceSearchButton, scanBarcodeButton;
    private LinearLayout resultsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search); // Updated layout reference

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        voiceSearchButton = findViewById(R.id.voice_search_button);
        scanBarcodeButton = findViewById(R.id.btn_open_scanner);
        resultsContainer = findViewById(R.id.results_container);

        // Set up text search
        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString();
            if (!query.isEmpty()) {
                searchProducts(query);
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up voice search
        voiceSearchButton.setOnClickListener(v -> startVoiceSearch());

        // Set up barcode scanning
        scanBarcodeButton.setOnClickListener(v -> startBarcodeScanner());
    }

    // Search products by text
    private void searchProducts(String query) {
        resultsContainer.removeAllViews();

        ProductRepository productRepository = new ProductRepository(getApplication());
        productRepository.searchProducts(query).observe(
                this, products -> {
                    for (Product product : products) {
                        if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                            addProductToResults(product);
                        }
                    }
                }
        );
    }

    // Display a product in the results container
    private void addProductToResults(Product product) {
        TextView productView = new TextView(this);
        productView.setText(product.getName());
        productView.setPadding(16, 16, 16, 16);
        productView.setTextSize(18);
        productView.setBackgroundResource(android.R.drawable.editbox_dropdown_dark_frame);
        productView.setOnClickListener(v -> {
            Toast.makeText(this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        resultsContainer.addView(productView);
    }

    // Start voice search
    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // Start barcode scanner
    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0); // Use the back camera
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    // Handle results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            searchInput.setText(spokenText); // Set the spoken text into the input field
            searchProducts(spokenText);
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    String scannedBarcode = result.getContents();
                    searchInput.setText(scannedBarcode); // Set the scanned barcode into the input field
                    searchProducts(scannedBarcode); // Search with the scanned barcode
                }
            }
        }
    }
}
