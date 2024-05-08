package com.softclouds.SpringAdvancedConcepts.ratelimit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RateLimitException(final String message) {
        super(message);
    }

}
