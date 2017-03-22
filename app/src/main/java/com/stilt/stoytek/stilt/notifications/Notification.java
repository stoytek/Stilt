package com.stilt.stoytek.stilt.notifications;

import java.sql.Timestamp;

/**
 * Created by frodeja on 08/03/17.
 */

public class Notification {
    /* TODO: Create an interface called NotificationSource? */
    Object source;
    private String text;
    private Timestamp created; /* Is this really needed? */
    private double dBLevel;

    public Notification() {
        this.source = null;
        this.text = "<placeholder>";
        this.created = new Timestamp(System.currentTimeMillis());
        this.dBLevel = 0;
    }

    public Notification(String text, double dBLevel) {
        this.source = null;
        this.text = text;
        this.dBLevel = dBLevel;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Notification(String text, Timestamp created, double dBLevel) {
        this.source = null;
        this.text = text;
        this.created = created;
        this.dBLevel = dBLevel;
    }

    public Notification(Object source, String text, Timestamp created, double dBLevel) {
        this.source = source;
        this.text = text;
        this.created = created;
        this.dBLevel = dBLevel;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public double getdBLevel() {
        return dBLevel;
    }

    public void setdBLevel(double dBLevel) {
        this.dBLevel = dBLevel;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
