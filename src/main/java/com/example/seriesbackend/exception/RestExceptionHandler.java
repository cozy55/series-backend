package com.example.seriesbackend.exception;

import com.example.seriesbackend.dto.ExceptionResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RegistrationException.class)
    protected ResponseEntity<Object> registrationException(RegistrationException ex) {
        return buildResponseEntity(new ExceptionResponseDto(HttpStatus.CONFLICT, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ExceptionResponseDto exceptionResponseDto) {
        return new ResponseEntity<>(exceptionResponseDto, exceptionResponseDto.getHttpStatus());
    }
}
