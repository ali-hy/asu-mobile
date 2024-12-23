package com.dmm.ecommerceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class BarcodeScannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        Button scanButton = rootView.findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(v -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setCameraId(0); // Use the back camera
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
