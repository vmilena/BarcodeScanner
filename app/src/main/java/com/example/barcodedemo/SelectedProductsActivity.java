package com.example.barcodedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import butterknife.ButterKnife;

public class SelectedProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_products);
        ButterKnife.bind(this);
    }
}
