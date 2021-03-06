package com.stilt.stoytek.stilt.dtypes;

import android.support.annotation.NonNull;

import com.stilt.stoytek.stilt.db.SoundlevelDataSource;

import java.util.GregorianCalendar;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelMeasurement implements Comparable {

    private double dBval;
    private GregorianCalendar timestamp;

    /* Add other metadata as necessary */

    public SoundlevelMeasurement() {
        this.dBval = 0;
        this.timestamp = null;
    }

    public SoundlevelMeasurement(double dBval, GregorianCalendar timestamp) {
        this.dBval = dBval;
        this.timestamp = timestamp;
    }

    public double getdBval() {
        return dBval;
    }

    public GregorianCalendar getTimestamp() {
        return timestamp;
    }

    public long getTimestampMillis() {
        return timestamp.getTimeInMillis();
    }

    public void setdBval(double dBval) {
        this.dBval = dBval;
    }

    public void setTimestamp(GregorianCalendar timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        return (int) (((SoundlevelMeasurement)o).getTimestampMillis() - this.getTimestampMillis());
    }
}
