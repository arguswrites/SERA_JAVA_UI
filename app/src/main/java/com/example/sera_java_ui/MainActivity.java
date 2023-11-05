package com.example.sera_java_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextToSpeech tts;
    EditText ed1;
    Button btn;

    private static final int PERMISSION_REQUEST_CODE = 123;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText);
        btn=(Button)findViewById(R.id.button);

        checkAndRequestPermissions();

        final boolean[] isTtsInitialized = {false};

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language not supported.", Toast.LENGTH_SHORT).show();
                    } else {
                        isTtsInitialized[0] = true;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Initialization failed.", Toast.LENGTH_SHORT).show();
                    Log.d("TTS","Module Status: " + status);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            /** @noinspection deprecation*/
            @Override
            public void onClick(View v) {
                if (isTtsInitialized[0]) {
                    String toSpeak = ed1.getText().toString().trim();
                    if (!toSpeak.isEmpty()) {
                        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter text.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "TTS not initialized yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission denied. TTS may not work properly.", Toast.LENGTH_SHORT).show();
            checkAndRequestPermissions();  // Request permissions again
        }
    }

}