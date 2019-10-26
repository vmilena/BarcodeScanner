package com.example.barcodedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.barcodedemo.api.ApiHelper;
import com.example.barcodedemo.api.OnDataCallback;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.BarcodeModelBarcodeSpider;
import com.example.barcodedemo.api.models.BarcodeModelList;
import com.example.barcodedemo.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);
        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            //TODO Specify type of barcode
            Task<List<FirebaseVisionBarcode>> task = detector.detectInImage(image);
            task.addOnSuccessListener(firebaseVisionBarcodes -> {
                for (FirebaseVisionBarcode barcode :
                        firebaseVisionBarcodes) {
                    lookupInDatabases(barcode);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), "BARCODE DETECTION FAILED", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    private void lookupInDatabases(FirebaseVisionBarcode barcode) {
        checkUPCItemDB(barcode); // This is the first of three methods we call to check APIs
    }

    private void checkUPCItemDB(FirebaseVisionBarcode barcode) { //No. 1 Calls checkUPCDatabase() if API falls short
        ApiHelper.getInstance(Constants.API_SELECTION_UPCITEMDB).itemLookupUPCItemDB(barcode.getRawValue(), new OnDataCallback<BarcodeModelList>() {
            @Override
            public void onSuccess(BarcodeModelList data) {
                if (data != null) {
                    data.getItems().get(Constants.INDEX_FIRST).setImage(data.getItems().get(Constants.INDEX_FIRST).getImageUrl());
                    //TODO Maybe adjust models better
                    showProduct(data.getItems().get(Constants.INDEX_FIRST));
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

    private void checkUPCDatabase(FirebaseVisionBarcode barcode) { //No. 2 Called by checkUPCItemDB(), and calls checkBarcodeSpider() if API falls short
        ApiHelper.getInstance(Constants.API_SELECTION_UPCDATABASE).itemLookupUPCDatabase(barcode.getRawValue(), new OnDataCallback<BarcodeModel>() {
            @Override
            public void onSuccess(BarcodeModel data) {
                if (data != null) {
                    showProduct(data);
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

    private void checkBarcodeSpider(FirebaseVisionBarcode barcode) { //No.3 called by checkUPCDatabase()
        ApiHelper.getInstance(Constants.API_SELECTION_BARCODESPIDER).itemLookupBarcodeSpider(barcode.getRawValue(), new OnDataCallback<BarcodeModelBarcodeSpider>() {
            @Override
            public void onSuccess(BarcodeModelBarcodeSpider data) {
                if (data != null) {
                    showProduct(data.getBarcodeModel());
                } else {
                    Toast.makeText(getApplicationContext(), "SCANNED BARCODE WAS NOT FOUND.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), "SCANNED BARCODE WAS NOT FOUND.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showProduct(BarcodeModel data) {

        nameTextView.setText(data.getName() != null ? data.getName() : Constants.UNAVAILABLE);

        DecimalFormat df = new DecimalFormat("#.##");
        priceTextView.setText(data.getPrice() != 0 ? df.format(data.getPrice()) : Constants.UNAVAILABLE);
        descriptionTextView.setText(data.getDescription() != null ? data.getDescription() : Constants.UNAVAILABLE);
        if (data.getImage() != null) {
            Glide.with(this).load(data.getImage()).into(productImageView);
        }
        loader.setVisibility(View.GONE);
        productConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
