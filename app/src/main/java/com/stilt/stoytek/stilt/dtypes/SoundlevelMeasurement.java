package com.stilt.stoytek.stilt.dtypes;

import java.sql.Timestamp;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelMeasurement {

    private double dBval;
    private Timestamp timestamp;

    /* Add other metadata as necessary */

    public SoundlevelMeasurement() {
        this.dBval = 0;
        this.timestamp = null;
    }

    public SoundlevelMeasurement(double dBval, Timestamp timestamp) {
        this.dBval = dBval;
        this.timestamp = timestamp;
    }

    public double getdBval() {
        return dBval;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setdBval(double dBval) {
        this.dBval = dBval;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}
