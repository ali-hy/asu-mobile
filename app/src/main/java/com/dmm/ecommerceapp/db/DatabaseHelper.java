package com.dmm.ecommerceapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SECURITY_QUESTION = "security_question";
    private static final String COLUMN_SECURITY_ANSWER = "security_answer";

    // Table: Products
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    private static final String COLUMN_PRODUCT_CATEGORY_ID = "category_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_SECURITY_QUESTION + " TEXT, "
                + COLUMN_SECURITY_ANSWER + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Products Table
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_DESCRIPTION + " TEXT, "
                + COLUMN_PRODUCT_PRICE + " REAL, "
                + COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                + COLUMN_PRODUCT_CATEGORY_ID + " INTEGER)";
        db.execSQL(createProductsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create Products Table if it doesn't exist
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "("
                    + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PRODUCT_NAME + " TEXT, "
                    + COLUMN_PRODUCT_DESCRIPTION + " TEXT, "
                    + COLUMN_PRODUCT_PRICE + " REAL, "
                    + COLUMN_PRODUCT_QUANTITY + " INTEGER, "
                    + COLUMN_PRODUCT_CATEGORY_ID + " INTEGER)");
        }
    }


    // Check if email exists
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Validate user login credentials
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Get user by email
    public String getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        String userId = null;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
        }

        return userId;
    }

    // Get security question for the email
    public String getSecurityQuestion(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_SECURITY_QUESTION},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String securityQuestion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECURITY_QUESTION));
            cursor.close();
            return securityQuestion;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    // Validate security answer
    public boolean validateSecurityAnswer(String email, String securityAnswer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_SECURITY_ANSWER},
                COLUMN_EMAIL + "=? AND " + COLUMN_SECURITY_ANSWER + "=?",
                new String[]{email, securityAnswer},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Update password
    public void updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
    }

    // Add user
    public boolean addUser(String email, String password, String securityQuestion, String securityAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_SECURITY_QUESTION, securityQuestion);
        values.put(COLUMN_SECURITY_ANSWER, securityAnswer);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // product helper functions for admin
    // Add a new product
    public boolean addProduct(String name, String description, double price, int quantity, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, categoryId);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    // Update an existing product
    public boolean updateProduct(int id, String name, String description, double price, int quantity, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, name);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        values.put(COLUMN_PRODUCT_PRICE, price);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_CATEGORY_ID, categoryId);

        int rowsUpdated = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
        return rowsUpdated > 0;
    }

    // Delete a product
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Search products by name
    public Cursor searchProducts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_NAME + " LIKE ?", new String[]{"%" + query + "%"});
    }


}
