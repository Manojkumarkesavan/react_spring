package org.example.spring.react.repository;

import org.example.spring.react.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUser1AndUser2(Long user1, Long user2);

    @Query("SELECT c FROM Conversation c WHERE " +
            "(c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Conversation> findByUsers(@Param("user1") Long user1, @Param("user2") Long user2);

    @Query("SELECT c FROM Conversation c WHERE c.user1 = :userId OR c.user2 = :userId")
    List<Conversation> findByUserId(@Param("userId") Long userId);
}