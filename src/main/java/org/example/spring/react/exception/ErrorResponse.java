package org.example.spring.react.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private String errorDescription;

    public ErrorResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errorCode = "GENERIC_ERROR"; // Default error code
        this.errorDescription = message; // Default description is the message
    }
}
