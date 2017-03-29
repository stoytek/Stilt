package com.stilt.stoytek.stilt.audiorec;

import android.content.Context;
import android.util.Log;

import com.stilt.stoytek.stilt.db.SoundlevelDataSource;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by frodeja on 29/03/17.
 */

public class AudioRecorderWorker extends Thread implements AudioCallback {

    private static final String TAG = "AudioRecorderWorker";

    private final int waitTimeMillis = 100; //1000*10; /* Wait 10 seconds between each recording */
    private boolean keepAlive;

    private AudioRecorder audioRecorder;
    private SoundlevelDataSource slSrc;

    private Lock lock;
    private Condition audioReady;

    public AudioRecorderWorker(Context context) {
        slSrc = new SoundlevelDataSource(context);
        keepAlive = true;
        lock = new ReentrantLock();
        audioReady = lock.newCondition();
        audioRecorder = new AudioRecorder(lock, audioReady);
    }

    @Override
    public void run() {
        audioRecorder.createAudioRecorder();

        while (keepAlive) {
            lock.lock();
            audioRecorder.recordSample(this);
            try {
                Log.d(TAG, "Waiting...");
                audioReady.await();
                Log.d(TAG, "Got notified!");
            } catch (InterruptedException e) {
                /* TODO: Handle exception */
            } finally {
                lock.unlock();
            }

            double result = audioRecorder.getDBResult();
            Log.d(TAG, "Got result: " + result + " dB.");

            try {
                sleep(waitTimeMillis);
            } catch (InterruptedException e) {
                /* TODO: Handle exception */
            }

        }

    }

    public void notifyAudioReady() {
        Log.d(TAG, "Got notified that audio is ready");
        lock.lock();
        try {
            audioReady.signal();
        } finally {
            lock.unlock();
        }
    }

    public void kill() {
        keepAlive = false;
    }
}
