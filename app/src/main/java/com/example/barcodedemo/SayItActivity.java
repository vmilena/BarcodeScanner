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

import com.example.barcodedemo.adapters.TextResultsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SayItActivity extends AppCompatActivity implements ItemClickedInterface {

    private static final int MY_PERMISSIONS_RECORD_AUDIO = 99;
    @BindView(R.id.resultTextView)
    TextView resultTextView;
    @BindView(R.id.textResultsRecyclerView)
    RecyclerView textResultsRecyclerView;

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
        }
        updateText("Did not recognize what you said. Can you please repeat?");
    }

    private void updateText(String text) {
        resultTextView.setText(text);
    }

    private void performSearch(String searchQuery) {

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
    void stopSpeaking(){
        speechRecognizer.stopListening();
    }

    @Override
    public void itemClicked(String item) {
        Toast.makeText(this, "You tapped: " + item, Toast.LENGTH_SHORT).show();
        performSearch(item);
    }
}
