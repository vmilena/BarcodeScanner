package com.example.barcodedemo.api;

import com.example.barcodedemo.api.models.BarcodeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UPCDatabaseApiService {
    @GET("/product/{id}")
    Call<BarcodeModel> itemLookup(@Path("id") String barcode, @Query("apikey") String api_key);
}
