package com.example.barcodedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodedemo.adapters.ProductsAdapter;
import com.example.barcodedemo.adapters.TextResultsAdapter;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.orm.BarcodeScannerDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SayItActivity extends AppCompatActivity implements OnItemClickListener<String> {

    private static final int MY_PERMISSIONS_RECORD_AUDIO = 99;
    @BindView(R.id.resultTextView)
    TextView resultTextView;
    @BindView(R.id.textResultsRecyclerView)
    RecyclerView textResultsRecyclerView;
    @BindView(R.id.searchResultsRecyclerView)
    RecyclerView searchResultsRecyclerView;

    SpeechRecognizer speechRecognizer;
    private static final String TAG = SayItActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, SayItActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_it);
        ButterKnife.bind(this);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(TAG, "ready for speech");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "end of speech");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(TAG, "got some results");
                handleSpeechResults(bundle);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void handleSpeechResults(Bundle bundle) {
        ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (data != null) {
            updateText("Which of these terms do you want to search for?");
            TextResultsAdapter adapter = new TextResultsAdapter(this, data, this);
            textResultsRecyclerView.setAdapter(adapter);
        } else {
            updateText("Did not recognize what you said. Can you please repeat?");
        }
    }

    private void updateText(String text) {
        resultTextView.setText(text);
    }

    private void performSearch(String searchQuery) {
        String sqliteFriendlySearchQuery = "%" + searchQuery + "%";
        List<BarcodeModel> barcodeModels = BarcodeScannerDatabase.getInstance(getApplicationContext())
                .productDao()
                .getProductsByName(sqliteFriendlySearchQuery);
        updateResults(barcodeModels);
        if (barcodeModels.size() == 0) {
            Toast.makeText(this, "Didn't find any products that contain '" + searchQuery + "'.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateResults(List<BarcodeModel> barcodeModels) {
        ProductsAdapter adapter = new ProductsAdapter(barcodeModels, this, item -> ScannerActivity.start(SayItActivity.this, item));
        searchResultsRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.startSpeakingButton)
    void startSpeaking() {
        if (checkPermissions()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
            speechRecognizer.startListening(intent);
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            return false;
        }
        return true;
    }


    @OnClick(R.id.stopSpeakingButton)
    void stopSpeaking() {
        speechRecognizer.stopListening();
    }

    @Override
    public void itemClicked(String item) {
        performSearch(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
