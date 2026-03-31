package therapyapp.service;

import java.time.Instant;
import java.util.List;

import therapyapp.model.Message;
import therapyapp.repository.MessageRepository;

public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    
    public void sendMessage(String senderId, String receiverId, String message) {
        String conversationId = senderId + "_" + receiverId;
        Instant timestamp = Instant.now();

        Message newMessage = new Message(conversationId, senderId, receiverId, message, timestamp);
        messageRepository.saveMessage(newMessage);
    }


    public List<Message> getConversationHistory(String senderId, String receiverId) {
        String conversationId = senderId + "_" + receiverId;
        return messageRepository.getMessagesByConversationId(conversationId);
    }

    
    public List<Message> getAllSentMessages(String senderId) {
        return messageRepository.getMessagesBySenderId(senderId);
    }
}
