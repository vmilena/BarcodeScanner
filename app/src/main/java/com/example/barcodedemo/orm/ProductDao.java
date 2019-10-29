package com.example.barcodedemo.orm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.barcodedemo.api.models.BarcodeModel;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("Select * from products where userId like :userId")
    List<BarcodeModel> getAllProducts(int userId);

    @Query("Select * from products where upc like :upc")
    List<BarcodeModel> getProductList(String upc);

    @Query("Select * from products where name like :name")
    List<BarcodeModel> getProductsByName(String name);
    @Query("Select * from products")
    List<BarcodeModel> getAllProducts();
    @Insert
    void insertProduct(BarcodeModel barcodeModel);

    @Update
    void updateProduct(BarcodeModel barcodeModel);

    @Delete
    void deleteProduct(BarcodeModel barcodeModel);
}
