package com.stilt.stoytek.stilt;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.media.AudioRecord;
import android.media.AudioFormat;


public class MainActivity extends FragmentActivity {
    

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

    /* Check for valid sample rates, pass highest possible samplerate to createAudioEngine */
    /* TODO: This might be possible to do with OpenSLES
     *  SLAudioIODeviceCapabilitiesItf_->QuerySampleFormatsSupported(SLAudioIODeviceCapabilitiesItf self,
	 *	SLuint32 deviceId,
	 *	SLmilliHertz samplingRate,
	 *	SLint32 *pSampleFormats,
	 *	SLint32 *pNumOfSampleFormats);
     */

    private int getMaxSupportedSampleRate() {

        final int validSampleRates[] = new int[] { 8000, 11025, 16000, 22050,
                32000, 37800, 44056, 44100};

        for (int i = 0; i < validSampleRates.length; i++) {
            int result = AudioRecord.getMinBufferSize(validSampleRates[i],
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_DEFAULT);
            if (result != AudioRecord.ERROR
                    && result != AudioRecord.ERROR_BAD_VALUE && result > 0) {
                return validSampleRates[i];
            }
        }

        return -1;
    }

}
