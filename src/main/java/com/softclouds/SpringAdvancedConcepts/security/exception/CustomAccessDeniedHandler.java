package com.softclouds.SpringAdvancedConcepts.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softclouds.SpringAdvancedConcepts.exception.ErrorDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorDetail errorDetails = ErrorDetail.builder()
                .status(HttpStatus.valueOf(HttpServletResponse.SC_FORBIDDEN))
                .error(accessDeniedException.getMessage())
                .timestamp(LocalDateTime.now())
                .message("user not allowed to access the resource")
                .path(request.getServletPath())
                .build();
        log.error("User not Authorised {}", errorDetails.toString());

        final ObjectMapper mapper = new ObjectMapper();
        // register the JavaTimeModule, which enables Jackson to support Java 8 and higher date and time types
        mapper.registerModule(new JavaTimeModule());
        // ask Jackson to serialize dates as strings in the ISO 8601 format
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.writeValue(response.getOutputStream(), errorDetails);

    }
}
