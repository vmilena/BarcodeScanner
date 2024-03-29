package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodedemo.utils.UserManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.scanItButton)
    void scanIt() {
        openScannerActivity();
    }

    @OnClick(R.id.sayItButton)
    void sayIt() {
        openSayItActivity();
    }

    @OnClick(R.id.savedProductsButton)
    void savedProducts() {
        openSavedProducts();
    }

    @OnClick(R.id.signOutButton)
    void signOut() {
        UserManager.getInstance(this).logout();
        openSignInSignOutActivity();
    }

    @OnClick(R.id.browsingHistoryButton)
    void browsingHistory() {
        openBrowsingHistory();
    }

    @OnClick(R.id.searchBarcodeButton)
    void searchBarcode() {
        SearchBarcodeActivity.start(this);
    }

    private void openBrowsingHistory() {
        BrowsingHistoryActivity.start(this);
    }

    private void openSignInSignOutActivity() {
        SignInSignUpActivity.start(this);
    }

    private void openSavedProducts() {
        SavedProductsActivity.start(this);
    }

    private void openSayItActivity() {
        SayItActivity.start(this);
    }

    private void openScannerActivity() {
        ScannerActivity.start(this);
    }
}
