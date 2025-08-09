package com.devwonder.language_service.exception;

public class LanguageAlreadyExistsException extends RuntimeException {
    
    public LanguageAlreadyExistsException(String code) {
        super(String.format("Language with code '%s' already exists", code));
    }
    
    public LanguageAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}