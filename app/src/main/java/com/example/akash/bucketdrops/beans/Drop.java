package com.example.akash.bucketdrops.beans;

import io.realm.RealmObject;

/**
 * Created by knighthood on 3/31/2016.
 */
public class Drop extends RealmObject {
    private String what;
    private long time;
    private long when;
    private boolean completed;

    public Drop() {

    }

    public Drop(String what, long time, long when, boolean completed) {
        this.what = what;
        this.time = time;
        this.when = when;
        this.completed = completed;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
