package com.movieMatrix.exceptions;


public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }

}