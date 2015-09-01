package com.example.clothingstore.model;

import android.provider.BaseColumns;


public final class ClothingContract {
    public static final String DATABASE_NAME="Clothing.db";
    public static final int DATABASE_VERSION=1;

    public ClothingContract(){}; // para prevenir q esta clase se instancie x accidente

    public static abstract class ShirtTable implements BaseColumns{
        public static final String TABLE_NAME="shirts";
        public static final String COLUMN_DESCRIPTION="description";
        public static final String COLUMN_PRICE="price";
        public static final String COLUMN_STOCK="stock";
        public static final String COLUMN_COLOR="color";
    }

    // TODO: falta implementar las tablas de pantalones y sombreros



}
