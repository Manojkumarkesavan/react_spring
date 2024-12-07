package org.example.spring.react.controller;

import org.example.spring.react.domain.MessageDTO;
import org.example.spring.react.domain.MessageStatus;
import org.example.spring.react.entity.Conversation;
import org.example.spring.react.entity.Message;
import org.example.spring.react.repository.ConversationRepository;
import org.example.spring.react.repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MessageController {
    private final MessageRepository messageRepository;

    private final ConversationRepository conversationRepository;

    public MessageController(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }


    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            Optional<Conversation> conversationOpt = conversationRepository.findById(messageDTO.getConversationId());
            if (conversationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid conversation ID");
            }

            Message message = Message.builder()
                    .sender(messageDTO.getSender())
                    .receiver(messageDTO.getReceiver())
                    .content(messageDTO.getContent())
                    .timestamp(LocalDateTime.now())
                    .conversation(conversationOpt.get())
                    .status(MessageStatus.SENT)
                    .build();

            Message savedMessage = messageRepository.save(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving message");
        }
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
        try {
            List<Message> messages = messageRepository.findByConversation_Id(conversationId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving messages");
        }
    }

    @PutMapping("/messages/{messageId}/status")
    public ResponseEntity<?> updateMessageStatus(@PathVariable Long messageId, @RequestBody MessageStatus status) {
        try {
            Optional<Message> messageOpt = messageRepository.findById(messageId);
            if (messageOpt.isPresent()) {
                Message message = messageOpt.get();
                message.setStatus(status);
                messageRepository.save(message);
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating message status");
        }
    }

    @GetMapping("/users/{userId}/unread-counts")
    public ResponseEntity<Map<Long, Long>> getUnreadCounts(@PathVariable Long userId) {
        try {
            List<Object[]> results = messageRepository.countUnreadMessagesByConversationAndUser(
                    userId, MessageStatus.READ);

            Map<Long, Long> unreadCounts = results.stream()
                    .collect(Collectors.toMap(
                            result -> ((Number) result[0]).longValue(), // conversationId
                            result -> ((Number) result[1]).longValue()  // unreadCount
                    ));

            return ResponseEntity.ok(unreadCounts);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }

}
