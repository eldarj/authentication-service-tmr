
package com.ejahijagic.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of("error", "INTERNAL_ERROR", "message", ex.getMessage()));
    }
}
