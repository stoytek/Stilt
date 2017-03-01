package com.stilt.stoytek.stilt.audiorec;

import android.util.Log;

import android.media.AudioRecord;
import android.media.AudioFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by frodeja on 23/02/17.
 */

public class AudioRecorder {

    private int sampleRate; // Samplerate used when recording
    private AudioCallback cbObj; // Object to notify when audio is ready


    /* Load JNI native library */
    static {
        System.loadLibrary("audiorec");
    }

    public AudioRecorder() {
        setSampleRate(0);
        Log.d("AudioRecorder", "Entered constructor.");
        createEngine();

    }

    public boolean createAudioRecorder() {
        Log.d("AudioRecorder", "Entered createAudioRecorder.");
        List<Integer> sampleRates = getSupportedSampleRates(); //Get available samplerates
        Log.d("AudioRecorder", "Got supported samplerates.");
        setSampleRate(Collections.max(sampleRates)); // Set samplerate to highest available
        Log.d("AudioRecorder", "Set private samplerate field.");
        boolean success = createAudioRecorder(this.sampleRate); // Create an audiorecorder (in native library)
        Log.d("AudioRecorder", "Created audio recorder interface.");
        /* TODO: Perhaps loop over progressively lower samplerates if this fails? */
        return success;
    }

    public void recordSample(AudioCallback callbackObject) {
        this.cbObj = callbackObject;
        startRecord();
    }

    /* TODO: See if there is another way of doing this. */

    public void audioReady() {
        cbObj.notifyAudioReady();
        createAudioPlayer();
    }

    private List<Integer> getSupportedSampleRates() {

        final int validSampleRates[] = new int[] { 8000, 11025, 16000, 22050,
                32000, 37800, 44056, 44100};

        List<Integer> sampleRates = new ArrayList<Integer>();

        for (int i = 0; i < validSampleRates.length; i++) {
            int result = AudioRecord.getMinBufferSize(validSampleRates[i], AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            if (result != AudioRecord.ERROR
                    && result != AudioRecord.ERROR_BAD_VALUE && result > 0) {
                sampleRates.add(validSampleRates[i]);
            }
        }

        return Collections.unmodifiableList(sampleRates);
    }

    public int getSampleRate() {
        return sampleRate;
    }

    private void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }


    /* Native methods */
    public static native void createEngine();
    public static native boolean createAudioRecorder(int samplerate);
    public static native void destroy();
    public static native void createAudioPlayer();

    public native void startRecord();
}
