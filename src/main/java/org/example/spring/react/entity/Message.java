package org.example.spring.react.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sender;
    private Long receiver;
    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @JsonBackReference
    private Conversation conversation;

}
