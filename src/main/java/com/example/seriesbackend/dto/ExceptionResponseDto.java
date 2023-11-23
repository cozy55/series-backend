package com.example.seriesbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ExceptionResponseDto {
    HttpStatus httpStatus;
    String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    LocalDateTime timestamp;

    public ExceptionResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ExceptionResponseDto(HttpStatus httpStatus, Exception ex) {
        this.httpStatus = httpStatus;
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now();
    }
}
