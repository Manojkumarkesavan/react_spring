package org.example.spring.react.exception;


import lombok.Getter;

@Getter
public class UserAlreadyPresentException extends RuntimeException {

    private final String errorCode;
    private final String errorDescription;

    public UserAlreadyPresentException(String message, String errorCode, String errorDescription) {
        super(message);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
