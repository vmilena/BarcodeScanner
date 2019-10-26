package com.example.barcodedemo.api.models;

import com.example.barcodedemo.utils.Constants;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.LinkedList;
import java.util.List;

public class BarcodeModel extends SugarRecord<BarcodeModel> {
    @SerializedName("title")
    private String name;
    @SerializedName(value = "price", alternate = {"lowest_recorded_price", "msrp", "lowest_price"})
    private float price;
    private String description;
    @SerializedName("images")
    private List<String> imageUrls;
    private String image;

    public BarcodeModel() {
    }

    public BarcodeModel(String name, float price, String description, List<String> imageUrls) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrls = imageUrls;
        this.image = imageUrls.get(Constants.INDEX_FIRST);
    }


    public BarcodeModel(String name, float price, String description, String image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrls = new LinkedList<String>();
        this.imageUrls.add(image);
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
