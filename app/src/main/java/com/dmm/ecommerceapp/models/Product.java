/*package com.dmm.ecommerceapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private double price;
    private String barcode;
    private String imageUrl;
    private int quantity;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
*/

package com.dmm.ecommerceapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    private double price;
    private String barcode;
    private String imageUrl;

    // Default constructor (required for Room)
    public Product() {
    }

    // Full parameterized constructor
    public Product(String name, String description, double price, String barcode, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
    }

    // Simplified constructor for cases where barcode and imageUrl are not needed
    public Product(String name, double price, String description) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = null; // Optional, can be set later
        this.imageUrl = null; // Optional, can be set later
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}




