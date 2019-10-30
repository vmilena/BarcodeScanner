package com.example.barcodedemo.api;

import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.BarcodeModelBarcodeSpider;
import com.example.barcodedemo.api.models.BarcodeModelList;
import com.example.barcodedemo.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//This class helps with API communication

public class ApiHelper {
    Retrofit retrofit; //Retrofit is a library to retrieve and upload the data via Rest based web service

    private ApiHelper(String apiSelection) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //Logging the API response

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Gson gson = gsonBuilder.create(); //For mapping fields of API response and models

        OkHttpClient client = builder.build();

        switch (apiSelection) { //Since we are using 3 different APIs, we need to select the one that's being called
            case Constants.API_SELECTION_UPCITEMDB: {
                retrofit = getAdapter(Constants.BASE_URL_UPCITEMDB, client, gson);
                break;
            }
            case Constants.API_SELECTION_UPCDATABASE: {
                retrofit = getAdapter(Constants.BASE_URL_UPCDATABASE, client, gson);
                break;
            }
            case Constants.API_SELECTION_BARCODESPIDER: {
                retrofit = getAdapter(Constants.BASE_URL_BARCODESPIDER, client, gson);
                break;
            }
        }


    }

    public Retrofit getAdapter(String url, OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }


    public synchronized static ApiHelper getInstance(String apiSelection) {
        return new ApiHelper(apiSelection);
    }

    private <T> void processCall(Call<T> call, final OnDataCallback<T> onDataCallback) { //Method that sends generic request
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    onDataCallback.onSuccess(response.body());
                } else {
                    try {
                        onDataCallback.onFailure(Constants.FAILURE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onDataCallback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    public void itemLookupUPCItemDB(String barcode,
                                    OnDataCallback<BarcodeModelList> onDataCallback) {
        UPCItemDBApiService apiService = retrofit.create(UPCItemDBApiService.class);
        Call<BarcodeModelList> call = apiService.itemLookup(barcode);
        processCall(call, onDataCallback);
    }

    public void itemLookupUPCDatabase(String barcode, OnDataCallback<BarcodeModel> onDataCallback) {
        UPCDatabaseApiService apiService = retrofit.create(UPCDatabaseApiService.class);
        Call<BarcodeModel> call = apiService.itemLookup(barcode, Constants.UPCDATABASE_API_KEY);
        processCall(call, onDataCallback);
    }

    public void itemLookupBarcodeSpider(String barcode, OnDataCallback<BarcodeModelBarcodeSpider> onDataCallback) {
        BarcodeSpiderApiService apiService = retrofit.create(BarcodeSpiderApiService.class);
        Call<BarcodeModelBarcodeSpider> call = apiService.itemLookup(Constants.BARCODESPIDER_API_KEY, barcode);
        processCall(call, onDataCallback);
    }
}
