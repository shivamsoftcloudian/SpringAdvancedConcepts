package com.softclouds.SpringAdvancedConcepts.exception;

import com.softclouds.SpringAdvancedConcepts.ratelimit.RateLimitException;
import com.softclouds.SpringAdvancedConcepts.security.exception.InvalidJwtException;
import com.softclouds.SpringAdvancedConcepts.security.exception.XSSAttackException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, Exception ex, HttpServletRequest request) {

        ErrorDetail errorDetail = ErrorDetail.builder()
                .status(status)
                .statusCode(status.value())
                .error(status.name())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        log.error(errorDetail.toString());
        return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
    }

    @ExceptionHandler(ElementNotExistException.class)
    protected ResponseEntity<Object> handleElementNotExistException(HttpServletRequest request, ElementNotExistException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(ElementAlreadyExistException.class)
    protected ResponseEntity<Object> handleElementAlreadyExistException(HttpServletRequest request, ElementAlreadyExistException e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(InvalidJwtException e, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Object> handleRateLimitExceptionException(RateLimitException e, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.TOO_MANY_REQUESTS, e, request);
    }

    @ExceptionHandler(XSSAttackException.class)
    public ResponseEntity<Object> handleXSSAttackException(RateLimitException e, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleOtherExceptions(HttpServletRequest request, Exception e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, request);
    }

}
