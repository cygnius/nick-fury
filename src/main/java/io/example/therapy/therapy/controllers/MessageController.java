
package io.example.therapy.therapy.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.entity.Message;
import io.example.therapy.therapy.services.Impl.MessagesServiceImpl;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessagesServiceImpl messagesService;

    // Create a new message
    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messagesService.createMessage(message);
            return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Get messages sent by the current user
    @SuppressWarnings("null")
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Message>> getMessagesSentByCurrentUser(@PathVariable String senderId) {
        try {
            List<Message> messages = messagesService.messagesSentByCurrentUser(senderId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Get messages received by the current user
    @SuppressWarnings("null")
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesReceivedByCurrentUser(@PathVariable String receiverId) {
        try {
            List<Message> messages = messagesService.messagesReceivedByCurrentUser(receiverId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Get messages between a specific sender and receiver
    @SuppressWarnings("null")
    @GetMapping("/conversation/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesBetweenSenderAndReceiver(
            @PathVariable String senderId, @PathVariable String receiverId) {
        try {
            List<Message> messages = messagesService.findMessagesBySenderAndReceiver(senderId, receiverId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Get the conversation of the current user with a specific sender and receiver
    @SuppressWarnings("null")
    @GetMapping("/conversation/current/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getConversationOfCurrentUser(
            @PathVariable String senderId, @PathVariable String receiverId) {
        try {
            List<Message> messages = messagesService.findConversationOfCurrentUser(senderId, receiverId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Get all messages of the current user
    @SuppressWarnings("null")
    @GetMapping("/all/{senderId}")
    public ResponseEntity<List<Message>> getAllMessagesOfCurrentUser(@PathVariable String senderId) {
        try {
            List<Message> messages = messagesService.getAllMessagesOfByCurrentUser(senderId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

    // Search messages by a keyword
    @SuppressWarnings("null")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Message>> searchMessagesByKeyword(@PathVariable String keyword) {
        try {
            List<Message> messages = messagesService.searchMessagesByKeyword(keyword);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
}
