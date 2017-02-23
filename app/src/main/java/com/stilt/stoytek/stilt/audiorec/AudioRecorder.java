package com.stilt.stoytek.stilt.audiorec;

/**
 * Created by frodeja on 23/02/17.
 */

public class AudioRecorder {

    static {
        System.loadLibrary("audio-recorder-jni");
    }

    public AudioRecorder() {

        createEngine();

    }

    public void recordSample(AudioCallback callbackObject) {

    }


    /* Native methods */

    public static native void createEngine();
    public static native void createAudioRecorder(int samplerate);
    public static native void startRecord();


}
