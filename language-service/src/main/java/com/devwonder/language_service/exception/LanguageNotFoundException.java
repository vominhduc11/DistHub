package com.devwonder.language_service.exception;

public class LanguageNotFoundException extends RuntimeException {
    
    public LanguageNotFoundException(String code) {
        super(String.format("Language with code '%s' not found", code));
    }
    
    public LanguageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}