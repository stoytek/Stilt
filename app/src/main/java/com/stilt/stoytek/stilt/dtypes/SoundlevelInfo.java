package com.stilt.stoytek.stilt.dtypes;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelInfo {
    private double dB;
    private String info;

    public SoundlevelInfo() {
        this.dB = 0;
        this.info = "<placeholder>";
    }

    public SoundlevelInfo(double dB, String info) {
        this.dB = dB;
        this.info = info;
    }

    public double getdB() {
        return dB;
    }

    public void setdB(double dB) {
        this.dB = dB;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return String.valueOf(dB) + "dB: " + info;
    }
}
