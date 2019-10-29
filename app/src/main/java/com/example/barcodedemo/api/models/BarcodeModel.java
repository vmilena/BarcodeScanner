package com.example.barcodedemo.api.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.barcodedemo.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

@Entity(tableName = "products", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userId"), indices = {@Index("userId")})
public class BarcodeModel {
    @SerializedName("title")
    @ColumnInfo
    private String name;
    @SerializedName(value = "price", alternate = {"lowest_recorded_price", "msrp", "lowest_price"})
    @ColumnInfo
    private float price;
    @ColumnInfo
    private String description;
    @SerializedName("images")
    @Ignore
    private List<String> imageUrls;
    @ColumnInfo
    private String image;
    @SerializedName(value = "upc", alternate = {"barcode"})
    @PrimaryKey
    @NonNull
    private String upc;
    @ColumnInfo
    private String userId;

    public BarcodeModel(String name, float price, String description, String image, String upc, String userId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.upc = upc;
        this.userId = userId;
    }

    @Ignore
    public BarcodeModel() {
    }

    @Ignore
    public BarcodeModel(String name, float price, String description, List<String> imageUrls, String upc) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrls = imageUrls;
        this.image = imageUrls.get(Constants.INDEX_FIRST);
        this.upc = upc;
    }

    @Ignore
    public BarcodeModel(String name, float price, String description, String image, String upc) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.upc = upc;
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
