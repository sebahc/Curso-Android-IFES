package com.example.clothingstore.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


// All CRUD (create, read, update, delete)
public class ShirtDataSource {

    private SQLiteDatabase database;
    private ClothingHelper dbHelper;
    private String[] allColumns = {
            ClothingContract.ShirtTable._ID,
            ClothingContract.ShirtTable.COLUMN_DESCRIPTION,
            ClothingContract.ShirtTable.COLUMN_PRICE,
            ClothingContract.ShirtTable.COLUMN_STOCK,
            ClothingContract.ShirtTable.COLUMN_COLOR
    };

    public ShirtDataSource(Context context){
        dbHelper= new ClothingHelper(context);
    }

    // open
    public void open() throws SQLException{
        database=dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Shirt cursorToShirt(Cursor cursor){
        Shirt mShirt= new Shirt();
        mShirt.setID(cursor.getLong(0));
        mShirt.setDescription(cursor.getString(1));
        mShirt.setPrice(cursor.getDouble(2));
        mShirt.setQuantityInStock(cursor.getInt(3));
        mShirt.setColorCode(cursor.getString(4).charAt(0));
        return mShirt;
    }

    public Long addShirt(Shirt mShirt){
        ContentValues values = new ContentValues();
        values.put(ClothingContract.ShirtTable.COLUMN_DESCRIPTION, mShirt.getDescription());
        values.put(ClothingContract.ShirtTable.COLUMN_PRICE, mShirt.getPrice());
        values.put(ClothingContract.ShirtTable.COLUMN_STOCK, mShirt.getQuantityInStock());
        values.put(ClothingContract.ShirtTable.COLUMN_COLOR, Character.toString(mShirt.getColorCode()));
        long insertId=database.insert(
                ClothingContract.ShirtTable.TABLE_NAME,
                null,
                values);
        return insertId;

    }

    // Read Method
    public Shirt getShirt(long id) {

        String strFilter = "_id= " + id + " ";
        Cursor cursor = database.query(
                ClothingContract.ShirtTable.TABLE_NAME,
                allColumns,
                strFilter,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        Shirt shirt = cursorToShirt(cursor);

        // Make sure to close the cursor
        cursor.close();
        return shirt;
    }

    //Update Method
    public void updateShirt(Shirt mShirt) {
        ContentValues values = new ContentValues();
        values.put(ClothingContract.ShirtTable.COLUMN_DESCRIPTION, mShirt.getDescription());
        values.put(ClothingContract.ShirtTable.COLUMN_PRICE, mShirt.getPrice());
        values.put(ClothingContract.ShirtTable.COLUMN_STOCK, mShirt.getQuantityInStock());
        values.put(ClothingContract.ShirtTable.COLUMN_COLOR, Character.toString(mShirt.getColorCode()));
        String strFilter = ClothingContract.ShirtTable._ID+ " = " + mShirt.getID();
        database.update(
                ClothingContract.ShirtTable.TABLE_NAME,
                values,
                strFilter,
                null);
    }

    // Delete Method
    public void deleteShirt(long id) {
        String strFilter = ClothingContract.ShirtTable._ID+ " = " + id;
        database.delete(
                ClothingContract.ShirtTable.TABLE_NAME,
                strFilter,
                null);
    }

    // Getting all Shirts
    public List<Shirt> getAllShirt() {
        List<Shirt> shirts = new ArrayList<Shirt>();
        Cursor cursor = database.query(
                ClothingContract.ShirtTable.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Shirt shirt = cursorToShirt(cursor);
            shirts.add(shirt);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return shirts;
    }


    public Cursor getAllShirt2() {
        Cursor mCursor = database.query(
                ClothingContract.ShirtTable.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);


        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor find(String filter) {
        Cursor mCursor = database.query(
                ClothingContract.ShirtTable.TABLE_NAME,
                allColumns,
                ClothingContract.ShirtTable.COLUMN_DESCRIPTION+" like '%"+filter+"%'",
                null,
                null,
                null,
                null);


        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }








}
