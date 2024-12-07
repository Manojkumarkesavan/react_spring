package org.example.spring.react.controller;

import org.example.spring.react.entity.Conversation;
import org.example.spring.react.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationRepository conversationRepository;

    public ConversationController(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrGetConversation(@RequestBody Map<String, Long> users) {
        Long user1 = users.get("user1");
        Long user2 = users.get("user2");

        Optional<Conversation> existingConversation = conversationRepository.findByUsers(user1, user2);

        if (existingConversation.isPresent()) {
            return ResponseEntity.ok(existingConversation.get());
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setUser1(user1);
            newConversation.setUser2(user2);
            Conversation savedConversation = conversationRepository.save(newConversation);
            return ResponseEntity.ok(savedConversation);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getConversations(@PathVariable Long userId) {
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        return ResponseEntity.ok(conversations);
    }

}
