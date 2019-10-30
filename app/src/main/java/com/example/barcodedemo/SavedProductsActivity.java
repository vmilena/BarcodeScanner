package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedProductsActivity extends AppCompatActivity {

    @BindView(R.id.productsRecyclerView)
    RecyclerView productsRecyclerView;

    public static void start(Context context) {
        Intent starter = new Intent(context, SavedProductsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_products);
        ButterKnife.bind(this);
    }
}
