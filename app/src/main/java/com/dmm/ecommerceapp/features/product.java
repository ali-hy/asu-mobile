package com.dmm.ecommerceapp.features;

public class product {
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;

    public product(String name, String description, double price, int quantity, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

