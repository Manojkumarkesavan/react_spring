package org.example.spring.react.controller;

import org.example.spring.react.entity.Message;
import org.example.spring.react.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        // Save the message to the database
        Message savedMessage = messageRepository.save(message);

        // Send the message to subscribers of /topic/messages/{conversationId}
        String destination = "/topic/messages/" + message.getConversation().getId();
        messagingTemplate.convertAndSend(destination, savedMessage);
    }

    @PostMapping("/api/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/api/conversations/{conversationId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return ResponseEntity.ok(messages);
    }

}
