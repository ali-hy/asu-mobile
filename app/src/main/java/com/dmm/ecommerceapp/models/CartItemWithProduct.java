package com.dmm.ecommerceapp.models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CartItemWithProduct {
    @Embedded
    public CartItem cartItem;

    @Relation(
            parentColumn = "productId",
            entityColumn = "id"
    )
    public Product product;
}
