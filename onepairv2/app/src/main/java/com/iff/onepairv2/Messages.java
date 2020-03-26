package com.iff.onepairv2;

/**
 * Each Messages objects contains text of the message, UID of who it is from and the time sent
 * @author ifandonlyif
 */
public class Messages {

    /**
     * The text sent in the message
     */
    private String message;
    /**
     * The time in which the message is sent
     */
    private long time;
    /**
     * The UID of the sender of the message
     */
    private String from;

    /**
     * Constructor of Messages Object
     */
    public Messages(){

    }

    /**
     * Constructor of Messages object
     * @param message
     * @param time
     * @param from
     */
    public Messages(String message, long time, String from) {
        this.message = message;
        this.time = time;
        this.from = from;
    }

    /**
     * Return UID of sender of the message
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets UID of the sender of the message
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Returns text sent in the message
     * @return message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets text in the message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns time sent of message
     * @return time of message sent
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time of message sent
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }
}
