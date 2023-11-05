package com.example.sera_java_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextToSpeech tts;
    EditText ed1;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText);
        btn=(Button)findViewById(R.id.button);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language not supported.", Toast.LENGTH_SHORT).show();
                    } else {
                        // TTS is ready
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Initialization failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            /** @noinspection deprecation*/
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString().trim();
                if (!toSpeak.isEmpty()) {
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();

                    // Add a delay before speaking
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }, 1000); // Adjust the delay as needed
                } else {
                    Toast.makeText(MainActivity.this, "Please enter text.", Toast.LENGTH_SHORT).show();
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
}