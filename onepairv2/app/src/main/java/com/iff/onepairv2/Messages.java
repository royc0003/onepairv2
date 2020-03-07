package com.iff.onepairv2;

public class Messages {

    private String message;
    private long time;
    private String from;

    public Messages(String message, long time, String from) {
        this.message = message;
        this.time = time;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
