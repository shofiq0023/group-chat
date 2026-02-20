package com.shofiqul.socket.exceptions;

import com.shofiqul.socket.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Validation errors (@Valid)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Map<String, String>>>> handleValidationException(
            WebExchangeBindException ex) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, duplicate) -> existing
                ));

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(fieldErrors, "Validation failed")));
    }

    // 404 Not Found
    @ExceptionHandler(NoSuchElementException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleNotFoundException(
            NoSuchElementException ex) {

        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(null, ex.getMessage())));
    }

    // Fallback for everything else
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleGenericException(Exception ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(null, "An unexpected error occurred")));
    }
}
