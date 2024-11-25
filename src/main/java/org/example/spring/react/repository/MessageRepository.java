package org.example.spring.react.repository;

import org.example.spring.react.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
}