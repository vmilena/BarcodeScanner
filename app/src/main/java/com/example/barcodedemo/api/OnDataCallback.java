package com.example.barcodedemo.api;

public interface OnDataCallback<T> {
    void onSuccess(T data);
    void onFailure(String message);
}
