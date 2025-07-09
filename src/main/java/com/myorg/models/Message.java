package com.myorg.models;


public class Message {
    private String id;
    private String sender_id;
    private String recipient_id;
    private String content;
    private String sender_receiver_sent_at;
    private String receiver_sender_sent_at;
    private String timestamp;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSender_id() { return sender_id; }
    public void setSender_id(String sender_id) { this.sender_id = sender_id; }

    public String getRecipient_id() { return recipient_id; }
    public void setRecipient_id(String recipient_id) { this.recipient_id = recipient_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSender_receiver_sent_at() { return sender_receiver_sent_at; }
    public void setSender_receiver_sent_at(String value) { this.sender_receiver_sent_at = value; }

    public String getReceiver_sender_sent_at() { return receiver_sender_sent_at; }
    public void setReceiver_sender_sent_at(String value) { this.receiver_sender_sent_at = value; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
