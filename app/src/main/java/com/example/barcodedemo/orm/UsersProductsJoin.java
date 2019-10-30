package com.example.barcodedemo.orm;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.User;

@Entity(tableName = "users_products_join",
        primaryKeys = {"userId", "upc"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId"),
                @ForeignKey(entity = BarcodeModel.class,
                        parentColumns = "upc",
                        childColumns = "upc")
        }, indices = {@Index("upc")})

public class UsersProductsJoin {
        public int userId;
        @NonNull
        public String upc = "";
}
