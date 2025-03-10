package com.movieMatrix.exceptions;

import com.movieMatrix.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Custom Exception
//  @ExceptionHandler(CustomException.class)
//  public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
//    Map<String, Object> errorResponse = new HashMap<>();
//    errorResponse.put("timestamp", LocalDateTime.now());
//    errorResponse.put("message", ex.getMessage());
//    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
//    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//  }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
//    Map<String, Object> errorResponse = new HashMap<>();
//    errorResponse.put("timestamp", LocalDateTime.now());
//    errorResponse.put("message", "An unexpected error occurred!");
//    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

//  @ExceptionHandler(JwtGenerationException.class)
//    public ResponseEntity<Map<String, String>> handleJwtGenerationException(JwtGenerationException ex) {
////      log.error("JWT Generation failed: {}", ex.getMessage());
////    Map<String, Object> errorResponse = new HashMap<>();
////    errorResponse.put("timestamp", LocalDateTime.now());
////    errorResponse.put("message", "An unexpected error occurred!");
////    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//      return ResponseEntity
//              .status(HttpStatus.INTERNAL_SERVER_ERROR)
//              .body(Collections.singletonMap("error", "Authentication failed"));
//    }


//  @ExceptionHandler(AuthenticationException.class)
//  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
////    log.error("Authentication error: {}", ex.getMessage());
//    return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
//  }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
//    log.error("Invalid token error: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
//    log.error("Token expired: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserException ex) {
//    log.error("User not found: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}

