package com.example.barcodedemo.api;

import com.example.barcodedemo.api.models.BarcodeModelBarcodeSpider;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BarcodeSpiderApiService {
    @GET("lookup")
    Call<BarcodeModelBarcodeSpider> itemLookup(@Query("token") String api_key, @Query("upc") String barcode);
}
