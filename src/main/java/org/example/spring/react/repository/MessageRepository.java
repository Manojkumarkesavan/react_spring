package org.example.spring.react.repository;

import org.example.spring.react.domain.MessageStatus;
import org.example.spring.react.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation_Id(Long conversationId);

    @Query("SELECT m.conversation.id AS conversationId, COUNT(m) AS unreadCount " +
            "FROM Message m " +
            "WHERE m.receiver = :userId AND m.status != :readStatus " +
            "GROUP BY m.conversation.id")
    List<Object[]> countUnreadMessagesByConversationAndUser(@Param("userId") Long userId,
                                                            @Param("readStatus") MessageStatus readStatus);
}