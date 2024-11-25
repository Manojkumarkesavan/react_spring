package org.example.spring.react.controller;

import org.example.spring.react.entity.Conversation;
import org.example.spring.react.entity.Message;
import org.example.spring.react.repository.ConversationRepository;
import org.example.spring.react.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
public class ChatController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        // Find or create a conversation
        Conversation conversation = conversationRepository
                .findByUser1AndUser2(message.getSender(), message.getReceiver())
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setUser1(message.getSender());
                    newConversation.setUser2(message.getReceiver());
                    return conversationRepository.save(newConversation);
                });

        // Save the message
        message.setTimestamp(LocalDateTime.now());
        message.setConversation(conversation);
        return messageRepository.save(message);
    }
}
