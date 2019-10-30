package com.example.barcodedemo.orm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.barcodedemo.api.models.BarcodeModel;

import java.util.List;

@Dao
public interface UsersProductsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUserProduct(UsersProductsJoin usersProductsJoin);

    @Query("Select * from products " +
            "inner join users_products_join " +
            "on products.upc=users_products_join.upc " +
            "where users_products_join.userId=:userId")
    List<BarcodeModel> getProductsForUser(final int userId);

    @Query("Select * from products " +
            "inner join users_products_join " +
            "on users_products_join.upc=products.upc " +
            "where users_products_join.userId=:userId and users_products_join.upc=:upc")
    List<BarcodeModel> lookupSavedProductForUser(final int userId, final String upc);
}
