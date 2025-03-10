package com.movieMatrix.exceptions;

public class    TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}