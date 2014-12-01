package com.drotth.grumpychat;

public class ChatMessage {
    String id;
    String from;
    String message;
    String timestamp;

    public ChatMessage(){}

    public ChatMessage(String id, String from, String message, String timestamp) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}