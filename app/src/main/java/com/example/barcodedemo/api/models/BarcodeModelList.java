package com.example.barcodedemo.api.models;

import java.util.List;

public class BarcodeModelList {

    private List<BarcodeModel> items;

    public BarcodeModelList(List<BarcodeModel> items) {
        this.items = items;
    }

    public List<BarcodeModel> getItems() {
        return items;
    }

    public void setItems(List<BarcodeModel> items) {
        this.items = items;
    }
}
