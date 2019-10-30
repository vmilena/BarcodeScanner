package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodedemo.adapters.ProductsAdapter;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.orm.BarcodeScannerDatabase;
import com.example.barcodedemo.orm.ProductDao;
import com.example.barcodedemo.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowsingHistoryActivity extends AppCompatActivity {
    //Displays all browsed products from all users
    @BindView(R.id.productsRecyclerView)
    RecyclerView productsRecyclerView;
    List<BarcodeModel> products;
    ProductsAdapter productsAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, BrowsingHistoryActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);
        ButterKnife.bind(this);

        ProductDao productDao = BarcodeScannerDatabase.getInstance(this.getApplicationContext()).productDao();
        products = productDao.getAllProducts();
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ProductsAdapter(products, this, item -> showClickedProduct(item));
        productsRecyclerView.setAdapter(productsAdapter);
    }

    private void showClickedProduct(BarcodeModel item) {
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(Constants.PRODUCT, item);
        startActivity(intent);
        finish();
    }
}
