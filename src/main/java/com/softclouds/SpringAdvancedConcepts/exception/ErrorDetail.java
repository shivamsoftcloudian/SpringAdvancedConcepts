package com.softclouds.SpringAdvancedConcepts.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ErrorDetail {

    private final UUID id = UUID.randomUUID();
    private HttpStatus status;
    private int statusCode;
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String path;

    private ErrorDetail() {
        timestamp = LocalDateTime.now();
    }

    ErrorDetail(HttpStatus status) {
        this();
        this.status = status;
    }

    ErrorDetail(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.error = ex.getLocalizedMessage();
        this.message = "Unexpected Error";
    }

    ErrorDetail(HttpStatus status, String message, String debugMessage) {
        this();
        this.status = status;
        this.error = message;
        this.message = debugMessage;
    }

    ErrorDetail(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.error = message;
        this.message = ex.getLocalizedMessage();
    }
}
