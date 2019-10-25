package com.example.barcodedemo.api;

import com.example.barcodedemo.api.models.BarcodeModelList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UPCItemDBApiService {

    @GET("trial/lookup")
    Call<BarcodeModelList> itemLookup(@Query("upc") String barcode);
}
