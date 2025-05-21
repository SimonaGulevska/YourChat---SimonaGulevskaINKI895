package com.example.yourchat.model;
import com.google.firebase.Timestamp;

import java.util.List;

// Class for Chatroom
public class ChatroomModel {
    // Unique ID for the chatroom
    String chatroomId;
    // List of users participating in the chat
    List<String> userIds;
    // Timestamp of the last message
    Timestamp lastMessageTimestamp;
    // ID of the sender of the last message
    String lastMessageSenderId;
    // Content of the last message
    String lastMessage;

    // empty constructor required for Firebase during deserialization
    public ChatroomModel() {
    }
    // constructor to initialize fields
    public ChatroomModel(String chatroomId, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        this.chatroomId = chatroomId;
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }

    // Getter and setter methods
    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}