package com.example.texttospeech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.Manifest;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btnGenrateSpeech;
    protected static final int RESULT_SPEECH = 1;
    Button btnGenerateText;
    EditText etText;

    TextToSpeech t1;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                    t1.setLanguage(Locale.ENGLISH);
                //t1.setLanguage(Locale.getDefault());
                // t1.setSpeechRate(1.0f);
            }
        });

        btnGenrateSpeech = findViewById(R.id.btn);
        btnGenerateText = findViewById(R.id.btnLis);
        etText = findViewById(R.id.et);
        // tv = findViewById(R.id.textview);
        btnGenrateSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etText.getText().toString();
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnGenerateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    etText.setText("");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Incompativel com o seu dispositivo", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> text =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etText.setText(text.get(0));
                }
                break;
        }
    }
}


