package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CineFastSnacks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SNACKS = "snacks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_INFO = "info";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";

    public SnackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_INFO + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_IMAGE + " TEXT)";
        db.execSQL(createTable);
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        addSnack(db, "Popcorn", "Large / Buttered", 8.99f, "popcorn");
        addSnack(db, "Nachos", "With Cheese Dip", 7.99f, "nachos");
        addSnack(db, "Soft Drinks", "Large / Any Flavor", 5.99f, "drinks");
        addSnack(db, "Candy Mix", "Assorted Candies", 6.99f, "candy");
    }

    private void addSnack(SQLiteDatabase db, String name, String info, float price, String imageName) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_INFO, info);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, imageName);
        db.insert(TABLE_SNACKS, null, values);
    }

    public ArrayList<Snack> getAllSnacks(Context context) {
        ArrayList<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String info = cursor.getString(2);
                float price = cursor.getFloat(3);
                String imageName = cursor.getString(4);

                // Convert String name to resource ID
                int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

                snacks.add(new Snack(name, info, price, imageId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return snacks;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }
}