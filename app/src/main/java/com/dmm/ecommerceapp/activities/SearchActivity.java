package com.dmm.ecommerceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dmm.ecommerceapp.R;
import com.dmm.ecommerceapp.models.Product;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 1;

    private EditText searchInput;
    private Button searchButton, voiceSearchButton, barcodeSearchButton;
    private LinearLayout resultsContainer;
    private List<Product> allProducts; // Replace with your product data source

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        voiceSearchButton = findViewById(R.id.voice_search_button);
        barcodeSearchButton = findViewById(R.id.barcode_search_button);
        resultsContainer = findViewById(R.id.results_container);

        // Initialize product list
        allProducts = new ArrayList<>(); // Replace this with actual product data
        //populateSampleProducts(); // For demo purposes

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

        // Set up barcode search
        barcodeSearchButton.setOnClickListener(v -> scanBarcode());
    }

    // Search products by text
    private void searchProducts(String query) {
        resultsContainer.removeAllViews(); // Clear previous results
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                addProductToResults(product);
            }
        }
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

    // Handle voice search result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            searchProducts(spokenText);
        }
    }

    // Barcode search
    private void scanBarcode() {
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_EAN_13,
                                Barcode.FORMAT_UPC_A)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        // Here, you need to implement camera capture logic to get the image
        InputImage image = InputImage.fromBitmap(yourBitmap, 0); // Replace "yourBitmap" with actual captured image

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String barcodeValue = barcode.getRawValue();
                        searchProducts(barcodeValue); // Search by barcode value
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Barcode scanning failed", Toast.LENGTH_SHORT).show();
                });
    }

    // Populate sample products (for demo)
    /*private void populateSampleProducts() {
        allProducts.add(new Product("Apple"));
        allProducts.add(new Product("Banana"));
        allProducts.add(new Product("Laptop"));
        allProducts.add(new Product("Camera"));
    }*/
}

