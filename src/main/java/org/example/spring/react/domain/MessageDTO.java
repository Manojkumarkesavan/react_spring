package org.example.spring.react.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    private Long sender;
    private Long receiver;
    private String content;
    private Long conversationId;
    private MessageStatus status;
}
