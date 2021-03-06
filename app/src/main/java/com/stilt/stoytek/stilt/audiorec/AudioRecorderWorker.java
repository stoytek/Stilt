package com.stilt.stoytek.stilt.audiorec;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.stilt.stoytek.stilt.LydbelastningsFragment;
import com.stilt.stoytek.stilt.R;
import com.stilt.stoytek.stilt.db.SoundlevelDataSource;
import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.GregorianCalendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by frodeja on 29/03/17.
 */

public class AudioRecorderWorker extends Thread implements AudioCallback {

    private static final String TAG = "AudioRecorderWorker";

    private final int waitTimeMillis = 1000; /* Wait 10 seconds between each recording */
    private boolean keepAlive;

    Context context;
    private AudioRecorder audioRecorder;
    private SoundlevelDataSource slSrc;

    private Lock lock;
    private Condition audioReady;

    private Object mPauseLock;
    private boolean mPaused = false;



    public AudioRecorderWorker(Context context) {
        this.context = context;
        slSrc = new SoundlevelDataSource(context);
        keepAlive = true;
        lock = new ReentrantLock();
        audioReady = lock.newCondition();
        audioRecorder = new AudioRecorder();
        mPauseLock = new Object();
    }

    @Override
    public void run() {
        audioRecorder.createAudioRecorder();

        while (keepAlive) {
            lock.lock();
            audioRecorder.recordSample(this);
            long now = System.currentTimeMillis();
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

            slSrc.open();
            GregorianCalendar timestamp = new GregorianCalendar();
            timestamp.setTimeInMillis(now);
            long rc = slSrc.insertSoundlevelMeasurement(new SoundlevelMeasurement(result, timestamp));
            slSrc.close();
            Log.d(TAG, "Inserted sound level measurement into database, return code: "+rc);

            Fragment page = ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.activity_main + ":" + 0);
            LydbelastningsFragment oneFragment = (LydbelastningsFragment) page;
            oneFragment.setFunFact(""+result+" dB");

            try {
                sleep(waitTimeMillis);
            } catch (InterruptedException e) {
                /* TODO: Handle exception */
            }

            while (mPaused) {
                synchronized (mPauseLock) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {

                    }
                }
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

    public void onPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }

    }

    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }

    }
}
