package com.example.clothingstore.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sebahc on 8/31/15.
 */
public class ClothingHelper extends SQLiteOpenHelper{

    // Table creation
    private static final String SHIRT_TABLE_CREATE="create table "
            + ClothingContract.ShirtTable.TABLE_NAME+"("+ ClothingContract.ShirtTable._ID
            +" integer primary key autoincrement,"
            + ClothingContract.ShirtTable.COLUMN_DESCRIPTION+" text not null, "
            + ClothingContract.ShirtTable.COLUMN_PRICE+" real not null, "
            + ClothingContract.ShirtTable.COLUMN_STOCK+" integer not null, "
            + ClothingContract.ShirtTable.COLUMN_COLOR+" text not null, "
            +");";

    // Table delete
    private static final String SHIRT_TABLE_DELETE ="DROP TABLE IF EXIST "
            + ClothingContract.ShirtTable.TABLE_NAME;



    public ClothingHelper(Context context){
        super(context, ClothingContract.DATABASE_NAME,null, ClothingContract.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SHIRT_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("ClothingHelper","Upgrading DB");
        db.execSQL(SHIRT_TABLE_DELETE);
        onCreate(db);

    }


}
