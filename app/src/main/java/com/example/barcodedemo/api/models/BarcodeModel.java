package com.example.barcodedemo.api.models;

import com.example.barcodedemo.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarcodeModel {
    @SerializedName("title")
    private String name;
    @SerializedName(value = "price", alternate = {"lowest_recorded_price", "mrsp"})
    private float price;
    private String description;
    @SerializedName("images")
    private List<String> imageUrls;

    public BarcodeModel(String name, float price, String description, List<String> imageUrls) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrls.get(Constants.INDEX_FIRST); //get only the first image url on the list because we only need one
    }

    public void setImageUrls(String imageUrl) {
        this.imageUrls = imageUrls;
    }
}
