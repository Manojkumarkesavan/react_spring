package org.example.spring.react.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private String errorDescription;
}
