package com.movieMatrix.exceptions;


// Custom exception class for user-related issues
public class UserException extends RuntimeException {

    // Default constructor
    public UserException() {
        super();
    }

    // Constructor with a custom message
    public UserException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public UserException(Throwable cause) {
        super(cause);
    }
}
