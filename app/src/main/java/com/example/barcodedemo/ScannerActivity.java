package com.example.barcodedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.barcodedemo.api.ApiHelper;
import com.example.barcodedemo.api.OnDataCallback;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.BarcodeModelBarcodeSpider;
import com.example.barcodedemo.api.models.BarcodeModelList;
import com.example.barcodedemo.api.models.User;
import com.example.barcodedemo.orm.BarcodeScannerDatabase;
import com.example.barcodedemo.orm.ProductDao;
import com.example.barcodedemo.orm.UsersProductsDao;
import com.example.barcodedemo.orm.UsersProductsJoin;
import com.example.barcodedemo.utils.Constants;
import com.example.barcodedemo.utils.UserManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScannerActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    BarcodeModel product;
    @BindView(R.id.productConstraintLayout)
    View productConstraintLayout;
    @BindView(R.id.loader)
    View loader;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.priceTextView)
    TextView priceTextView;
    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @BindView(R.id.productImageView)
    ImageView productImageView;
    @BindView(R.id.saveProductButton)
    Button saveProductButton;

    public static void start(Context context, BarcodeModel barcodeModel) { //To start this activity for already known product (displaying product)
        Intent starter = new Intent(context, ScannerActivity.class);
        starter.putExtra(Constants.PRODUCT, barcodeModel);
        context.startActivity(starter);
    }

    public static void start(Context context, String barcode) { //To start this activity for already known barcode (search by typing a barcode)
        Intent starter = new Intent(context, ScannerActivity.class);
        starter.putExtra(Constants.BARCODE, barcode);
        context.startActivity(starter);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ScannerActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.PRODUCT)) {
            product = (BarcodeModel) getIntent().getSerializableExtra(Constants.PRODUCT);
            showProduct(product);
        } else if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.BARCODE)) {
            lookupInDatabases(getIntent().getStringExtra(Constants.BARCODE));
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() { //Accessing and starting the device camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //After the picture is taken
        super.onActivityResult(requestCode, resultCode, data);
        loader.setVisibility(View.VISIBLE);
        //TODO Image/camera rotation
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

            FirebaseVisionBarcodeDetectorOptions options =
                    new FirebaseVisionBarcodeDetectorOptions.Builder()
                            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                            .build();

            FirebaseVisionBarcodeDetector detector =
                    FirebaseVision.getInstance().getVisionBarcodeDetector(options);

            Task<List<FirebaseVisionBarcode>> task = detector.detectInImage(image); //Detecting the barcode in image
            task.addOnSuccessListener(firebaseVisionBarcodes -> {
                //Since it is possible to scan multiple barcodes in one image
                //In order to simplify the code we will always show the first one
                if (!firebaseVisionBarcodes.isEmpty()) {
                    lookupInDatabases(firebaseVisionBarcodes.get(Constants.INDEX_FIRST).getRawValue());
                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No barcodes found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(e -> {
                loader.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Barcode detection failed", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    private void lookupInDatabases(String barcode) {
        loader.setVisibility(View.VISIBLE);
        checkUPCItemDB(barcode); // This is the first of three methods we call to check APIs
        //API checking is chained because we need to be sure that we're calling exactly one API at a time
    }

    private void checkUPCItemDB(String barcode) { //No. 1 Calls checkUPCDatabase() if UPCItemDB API falls short
        ApiHelper.getInstance(Constants.API_SELECTION_UPCITEMDB)
                .itemLookupUPCItemDB(barcode, new OnDataCallback<BarcodeModelList>() {
                    @Override
                    public void onSuccess(BarcodeModelList data) {
                        if (data.getTotal() != 0) {
                            product = data.getItems().get(Constants.INDEX_FIRST);
                            if (product.getImageUrls().size() != 0) {
                                product.setImage(product.getImageUrl());
                            }
                            product.setUpc(barcode);
                            showProduct(product);
                        } else {
                            checkUPCDatabase(barcode);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        checkUPCDatabase(barcode);
                    }
                });

    }

    private void checkUPCDatabase(String barcode) { //No. 2 Called by checkUPCItemDB(), and calls checkBarcodeSpider() if UPCDatabase API falls short
        ApiHelper.getInstance(Constants.API_SELECTION_UPCDATABASE)
                .itemLookupUPCDatabase(barcode, new OnDataCallback<BarcodeModel>() {
                    @Override
                    public void onSuccess(BarcodeModel data) {
                        if (data.getSuccess()) {
                            product = data;
                            product.setUpc(barcode);
                            showProduct(product);
                        } else {
                            checkBarcodeSpider(barcode);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        checkBarcodeSpider(barcode);
                    }
                });
    }

    private void checkBarcodeSpider(String barcode) {
        //No.3 called by checkUPCDatabase(); Last API call
        ApiHelper.getInstance(Constants.API_SELECTION_BARCODESPIDER)
                .itemLookupBarcodeSpider(barcode, new OnDataCallback<BarcodeModelBarcodeSpider>() {
                    @Override
                    public void onSuccess(BarcodeModelBarcodeSpider data) {
                        if (data.hasResults()) {
                            product = data.getBarcodeModel();
                            product.setUpc(barcode);
                            showProduct(product);
                        } else {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Barcode was not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Barcode was not found.", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        finish();
                    }
                });
    }

    private void showProduct(BarcodeModel data) {

        nameTextView.setText(data.getName() != null ? data.getName() : Constants.UNAVAILABLE);

        DecimalFormat df = new DecimalFormat("#.##");
        priceTextView.setText(data.getPrice() != 0 ? "$ " + df.format(data.getPrice()) : Constants.UNAVAILABLE);
        descriptionTextView.setText(data.getDescription() != null ? data.getDescription() : Constants.UNAVAILABLE);
        if (data.getImage() != null) {
            Picasso.get().load(data.getImage()).into(productImageView);
        }
        saveProductToHistory(); //Every product APIs find is saved to browsing history
        loader.setVisibility(View.GONE);
        saveProductButton.setVisibility(View.VISIBLE);
        productConstraintLayout.setVisibility(View.VISIBLE);
    }

    private void saveProductToHistory() {
        ProductDao productDao = BarcodeScannerDatabase.getInstance(this.getApplicationContext()).productDao();
        if (productDao.getProductListByUPC(product.getUpc()).isEmpty()) { //Checking if the product has already been saved
            productDao.insertProduct(product);
        }

    }

    @OnClick(R.id.saveProductButton)
    public void saveProduct() {
        User user = UserManager.getInstance(this).getCurrentUser();
        UsersProductsDao usersProductsDao = BarcodeScannerDatabase.getInstance(this.getApplicationContext()).usersProductsDao();
        if (usersProductsDao.lookupSavedProductForUser(user.getUserId(), product.getUpc()).isEmpty()) {
            UsersProductsJoin usersProductsJoin = new UsersProductsJoin();
            usersProductsJoin.upc = product.getUpc();
            usersProductsJoin.userId = user.getUserId();
            usersProductsDao.insertUserProduct(usersProductsJoin);
            Toast.makeText(this, "Product saved.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You already saved this product.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.homeButton)
    public void goToHomeScreen() {
        MainActivity.start(this);
        finish();
    }
}
