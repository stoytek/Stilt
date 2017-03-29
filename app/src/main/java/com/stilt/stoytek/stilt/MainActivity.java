package com.stilt.stoytek.stilt;


import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import com.stilt.stoytek.stilt.audiorec.AudioCallback;
import com.stilt.stoytek.stilt.audiorec.AudioRecorder;
import com.stilt.stoytek.stilt.audiorec.AudioRecorderWorker;
import com.stilt.stoytek.stilt.db.SoundlevelDataSource;
import com.stilt.stoytek.stilt.db.SoundlevelInfoDataSource;
import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.GregorianCalendar;


public class MainActivity extends FragmentActivity implements AudioCallback {

    ViewPager viewpager;
    AudioRecorderWorker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager som fikser Swipe Views
        viewpager = (ViewPager) findViewById(R.id.activity_main);
        PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);

        worker = new AudioRecorderWorker(this);
        worker.start();


    }

    @Override
    public void notifyAudioReady() {

    }
}
