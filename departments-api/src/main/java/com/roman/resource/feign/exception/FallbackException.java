package com.roman.resource.feign.exception;

public class FallbackException extends RuntimeException {
    public FallbackException(String message) {
        super(message);
    }
}
