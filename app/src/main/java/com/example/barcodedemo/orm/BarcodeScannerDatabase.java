package com.example.barcodedemo.orm;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.User;

@Database(entities = {User.class, BarcodeModel.class}, exportSchema = false, version = 1)
public abstract class BarcodeScannerDatabase extends RoomDatabase {
    private static final String DB_NAME = "barcode_scanner_database";
    private static BarcodeScannerDatabase instance;

    public static synchronized BarcodeScannerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, BarcodeScannerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();

    public abstract ProductDao productDao();
}
