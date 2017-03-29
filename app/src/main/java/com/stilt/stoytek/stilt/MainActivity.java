package com.stilt.stoytek.stilt;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import com.stilt.stoytek.stilt.audiorec.AudioRecorder;
import com.stilt.stoytek.stilt.db.SoundlevelDataSource;
import com.stilt.stoytek.stilt.db.SoundlevelInfoDataSource;
import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.GregorianCalendar;
import com.stilt.stoytek.stilt.audiorec.AudioCallback;

public class MainActivity extends FragmentActivity implements AudioCallback, LydbelastningsFragment.OnLydbelastningListener {

    AudioRecorder audiorec;
    SoundlevelInfoDataSource funfactSource;
    SoundlevelDataSource soundLevelSource;
    ViewPager viewpager;
    PagerAdapter padapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager som fikser Swipe Views
        viewpager = (ViewPager) findViewById(R.id.activity_main);
        padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);


        Log.wtf("Main", "samma d");

//        LydbelastningsFragment lydbelastningsFragment = (LydbelastningsFragment) getSupportFragmentManager().findFragmentById(R.id.lydbelastningsView);
//        lydbelastningsFragment.setFunFact();

//        LydbelastningsFragment lydbelastningsFragment = (LydbelastningsFragment) getSupportFragmentManager().findFragmentByTag(padapter.getLydbelastningsTag());
//        Log.wtf("Main", padapter.getLydbelastningsTag());
//        if (lydbelastningsFragment == null) {
//            Log.wtf("Main", "fragment not found");
//        }
//        lydbelastningsFragment.setFunFact();

        

        audiorec = new AudioRecorder();
        funfactSource = new SoundlevelInfoDataSource(this.getApplicationContext());
        soundLevelSource = new SoundlevelDataSource(this.getApplicationContext());

    }

    public void setFunFactText() {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.activity_main + ":" + 0);
        LydbelastningsFragment oneFragment = (LydbelastningsFragment) page;
        oneFragment.setFunFact();
    }

    @Override
    public void notifyAudioReady() {

    }
}
