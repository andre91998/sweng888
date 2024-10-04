package com.example.practice3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // Database Name
    private static final String DB_NAME = "practice3DB";

    // Database version
    private static final int DB_VERSION = 1;

    //Column Names
    private static final String TABLE_NAME = "products";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String DESCRIPTION_COL = "description";
    private static final String SELLER_COL = "seller";
    private static final String PRICE_COL = "price";
    private static final String PICTURE_COL = "picture";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLITE query to set column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + SELLER_COL + " TEXT,"
                + PRICE_COL + " REAL,"
                + PICTURE_COL + " BLOB)";

        // execute query
        db.execSQL(query);
    }

    // this method is use to add new product table entry
    public void addNewProduct(String productName, String productDescription, String productSeller,
                              float productPrice, byte[] productPicture) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, productName);
        values.put(DESCRIPTION_COL, productDescription);
        values.put(SELLER_COL, productSeller);
        values.put(PRICE_COL, productPrice);
        values.put(PICTURE_COL, productPicture);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
