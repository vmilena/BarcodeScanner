package com.example.barcodedemo.api.models;

import com.example.barcodedemo.utils.Constants;

import java.util.HashMap;
import java.util.List;

public class BarcodeModelBarcodeSpider {
    private HashMap<String, String> item_attributes;
    private List<HashMap<String, String>> Stores;

    public BarcodeModelBarcodeSpider(HashMap<String, String> item_attributes, List<HashMap<String, String>> stores) {
        this.item_attributes = item_attributes;
        Stores = stores;
    }

    public BarcodeModel getBarcodeModel() {
        return new BarcodeModel(
                this.item_attributes.get("title"),
                Float.parseFloat(this.Stores.get(Constants.INDEX_FIRST).get("price")),
                this.item_attributes.get("description"),
                this.item_attributes.get("image"),
                this.item_attributes.get("upc"));
    }

    public HashMap<String, String> getItem_attributes() {
        return item_attributes;
    }

    public void setItem_attributes(HashMap<String, String> item_attributes) {
        this.item_attributes = item_attributes;
    }

    public List<HashMap<String, String>> getStores() {
        return Stores;
    }

    public void setStores(List<HashMap<String, String>> stores) {
        Stores = stores;
    }
}
