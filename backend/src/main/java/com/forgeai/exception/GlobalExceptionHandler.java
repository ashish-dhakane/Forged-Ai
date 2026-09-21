package com.forgeai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Global exception interceptor providing structured and developer-friendly error messages across all endpoints.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles resource lookup failures (HTTP 404).
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handles domain logic or parameter validation failures (HTTP 400).
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handles invalid username or password during authentication (HTTP 401).
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Invalid email or password");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // Handles unauthorized access attempts (HTTP 403).
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", "You do not have permission to access this resource");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // Formats bean validation constraint violations into a readable field map.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Catch-all handler for unexpected system exceptions (HTTP 500).
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        logger.error("Unhandled server exception: ", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected server error occurred. Please try again later."
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
