package com.danialrekhman.productservicenocturne.exception;

public class CustomAccessDeniedException extends RuntimeException {
    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
