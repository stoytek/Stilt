package com.stilt.stoytek.stilt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.AudioRecord;
import android.media.AudioFormat;

import com.stilt.stoytek.stilt.audiorec.AudioCallback;
import com.stilt.stoytek.stilt.audiorec.AudioRecorder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AudioCallback {

    private AudioRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recorder = new AudioRecorder();
        Log.d("MainActivity", "Created new AudioRecorder object.");

        recorder.createAudioRecorder();
        Log.d("MainActivity", "Created audiorecorder native interface");

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(Integer.toString(recorder.getSampleRate()));

        (findViewById(R.id.record)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int status = ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO);
                if (status != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            0);
                    return;
                }
                recordAudio();
            }
        });
    }

    private void recordAudio() {
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Recording");
        Button button = (Button) findViewById(R.id.record);
        button.setBackgroundColor(Color.RED);
        recorder.recordSample(this);
    }

    @Override
    public void notifyAudioReady() {
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Audio is ready");
        findViewById(R.id.record).setBackgroundColor(Color.GREEN);
    }
}
