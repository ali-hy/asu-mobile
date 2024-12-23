package com.dmm.ecommerceapp.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dmm.ecommerceapp.models.Category;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        super(context, android.R.layout.simple_spinner_item, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        // Customize the dropdown view here if needed
        TextView textView = (TextView) view.findViewById(android.R.id.text1); // Default ID for text in simple_spinner_item layout
        textView.setText(categoryList.get(position).getName());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This is for the spinner's main view (what's shown after the user selects an item)
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(categoryList.get(position).getName());
        return textView;
    }
}
