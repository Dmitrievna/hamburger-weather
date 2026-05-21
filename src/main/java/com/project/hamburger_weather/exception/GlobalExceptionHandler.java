package com.project.hamburger_weather.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //todo check
    @ExceptionHandler(RouteParsingException.class)
    public ResponseEntity<ErrorResponse> handleRouteParsing(RouteParsingException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Invalid response from routing service",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonError(JsonProcessingException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Error parsing JSON data",
                List.of(ex.getOriginalMessage())
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
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFound(TagNotFoundException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Tag not found",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            WebExchangeBindException ex) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        //todo remove        
        System.out.println(" ERROR MESSAGES " + details);

        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "Validation failed",
                details
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(err);
    }
}
