package com.dmm.ecommerceapp.models;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
    public static List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 999.99, "High-performance laptop with 16GB RAM."));
        products.add(new Product("Smartphone", 499.99, "5G-enabled smartphone with 128GB storage."));
        products.add(new Product("Headphones", 199.99, "Noise-cancelling wireless headphones."));
        products.add(new Product("Camera", 599.99, "Digital camera with 4K video recording."));
        return products;
    }
}
