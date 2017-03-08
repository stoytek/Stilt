package com.stilt.stoytek.stilt;


import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.TextView;
import com.stilt.stoytek.stilt.audiorec.AudioCallback;
    

public class MainActivity extends FragmentActivity implements AudioCallback {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("audiorec");
    }

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager som fikser Swipe Views
        viewpager = (ViewPager) findViewById(R.id.activity_main);
        PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText("Hello world!");
        
    }

    @Override
    public void notifyAudioReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) findViewById(R.id.sample_text);
                tv.setText("Audio is ready");
                findViewById(R.id.record).setBackgroundColor(Color.GREEN);
            }
        });
    }
}
