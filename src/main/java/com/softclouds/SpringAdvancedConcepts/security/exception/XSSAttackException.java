package com.softclouds.SpringAdvancedConcepts.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class XSSAttackException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public XSSAttackException(String message) {
        super(message);
    }

    public XSSAttackException(String message, Throwable cause) {
        super(message, cause);
    }
}
