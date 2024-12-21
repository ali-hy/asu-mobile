package com.dmm.ecommerceapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item_table")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long productId;
    private int quantity;
    private double totalPrice;
    private String productName; // Add this field

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName; // Getter for productName
    }

    public void setProductName(String productName) {
        this.productName = productName; // Setter for productName
    }
}
