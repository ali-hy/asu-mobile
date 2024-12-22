package com.dmm.ecommerceapp.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dmm.ecommerceapp.models.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, android.R.layout.simple_spinner_item, productList);
        this.context = context;
        this.productList = productList;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // For dropdown view
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        // Customize the dropdown view here if needed
        TextView textView = (TextView) view.findViewById(android.R.id.text1); // Default ID for text in simple_spinner_item layout
        textView.setText(productList.get(position).getName());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This is for the spinner's main view (what's shown after the user selects an item)
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(productList.get(position).getName());
        return textView;
    }
}
