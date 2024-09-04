package com.example.quranmentor.models;

import java.util.HashMap;
import java.util.Map;

public class ChatingSystem {
    private String message;
    private String messageID;
    private long timestamp;
    private String userID;
    private String receiverID;
    private boolean read;

    public ChatingSystem() {}
    public ChatingSystem(String message,String messageID ,long timestamp,  String userID , String receiverID , boolean read) {
        this.message = message;
        this.messageID = messageID;
        this.timestamp = timestamp;
        this.userID = userID;
        this.receiverID = receiverID;
        this.read = read;

    }
    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
    public String getReceiverID() {
        return receiverID;
    }
    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
    public String getMessageID() {
        return messageID;
    }
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
