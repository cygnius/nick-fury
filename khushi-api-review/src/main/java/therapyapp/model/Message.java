package therapyapp.model;

public class Message {
    private String messageId;
    private String conversationId; 
    private String senderId;
    private String receiverId;
    private String message;
    private String timestamp; 

    public Message() {}

    public Message(String messageId, String conversationId, String senderId, String receiverId, String message, String timestamp) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
