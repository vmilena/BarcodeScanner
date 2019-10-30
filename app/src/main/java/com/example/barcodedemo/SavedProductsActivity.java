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
import com.example.barcodedemo.orm.UsersProductsDao;
import com.example.barcodedemo.utils.UserManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedProductsActivity extends AppCompatActivity {
    List<BarcodeModel> userProducts;
    ProductsAdapter productsAdapter;
    @BindView(R.id.savedProductsRecyclerView)
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

        UsersProductsDao usersProductsDao = BarcodeScannerDatabase.getInstance(this.getApplicationContext()).usersProductsDao();
        userProducts = usersProductsDao.getProductsForUser(UserManager.getInstance(this).getCurrentUser().getUserId());
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ProductsAdapter(userProducts, this, item -> showClickedProduct(item));
        productsRecyclerView.setAdapter(productsAdapter);

    }

    private void showClickedProduct(BarcodeModel item) {
        ScannerActivity.start(SavedProductsActivity.this, item);
        finish();
    }
}
