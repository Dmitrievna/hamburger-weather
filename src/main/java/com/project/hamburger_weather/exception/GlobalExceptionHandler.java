package com.project.hamburger_weather.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.hamburger_weather.dto.ErrorResponse;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RouteParsingException.class)
    public ResponseEntity<ErrorResponse> handleRouteParsing(RouteParsingException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Invalid response from routing service",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonError(JsonProcessingException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Error parsing JSON data",
                ex.getOriginalMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<String> handleMappingException(MappingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Failed to map external data: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Unexpected error",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
