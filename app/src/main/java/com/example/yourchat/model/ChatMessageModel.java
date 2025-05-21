package com.example.yourchat.model;
import com.google.firebase.Timestamp;

// Class for one Message in Chat
public class ChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;

    // empty constructor required for Firebase during deserialization
    public ChatMessageModel() {
    }

    // constructor to initialize all fields
    public ChatMessageModel(String message, String senderId, Timestamp timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    // Getter and setter methods
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}