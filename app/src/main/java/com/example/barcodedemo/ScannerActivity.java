package com.example.barcodedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.barcodedemo.api.ApiHelper;
import com.example.barcodedemo.api.OnDataCallback;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.api.models.BarcodeModelBarcodeSpider;
import com.example.barcodedemo.api.models.BarcodeModelList;
import com.example.barcodedemo.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

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
            task.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                @Override
                public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                    for (FirebaseVisionBarcode barcode :
                            firebaseVisionBarcodes) {
                        checkBarcodeSpider(barcode);
                        //checkUPCItemDB(barcode);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("BARCODE DETECTION FAILED"); //
                }
            });
        }
    }

    private void checkUPCItemDB(FirebaseVisionBarcode barcode) { //No. 1 Calls checkUPCDatabase() if API falls short
        ApiHelper.getInstance(Constants.API_SELECTION_UPCITEMDB).itemLookupUPCItemDB(barcode.getRawValue(), new OnDataCallback<BarcodeModelList>() {
            @Override
            public void onSuccess(BarcodeModelList data) {
                if (data != null) {
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
                    BarcodeModel item = data.getBarcodeModel();
                    showProduct(data.getBarcodeModel());
                } else {
                    System.out.println("SCANNED BARCODE WAS NOT FOUND.");
                }

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void showProduct(BarcodeModel data) {
        //TODO What do we do with product info
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
