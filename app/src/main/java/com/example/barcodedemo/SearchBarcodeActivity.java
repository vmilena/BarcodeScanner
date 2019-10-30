package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodedemo.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchBarcodeActivity extends AppCompatActivity {

    @BindView(R.id.searchEditText)
    EditText searchEditText;

    public static void start(Context context) {
        Intent starter = new Intent(context, SearchBarcodeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_barcode);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.searchButton)
    void searchBarcode() {
        String barcode = searchEditText.getText().toString();
        if (!barcode.isEmpty()) {
            Intent intent = new Intent(this, ScannerActivity.class);
            intent.putExtra(Constants.BARCODE, barcode);
            startActivity(intent);
        } else {
            Toast.makeText(this, "You did not type in barcode to search.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
