package com.example.seriesbackend.exception;

public class FailedParsingException extends RuntimeException{
    public FailedParsingException(String message) {
        super(message);
    }

    public FailedParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
