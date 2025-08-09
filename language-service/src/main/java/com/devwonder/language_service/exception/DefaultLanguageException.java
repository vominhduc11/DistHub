package com.devwonder.language_service.exception;

public class DefaultLanguageException extends RuntimeException {
    
    public DefaultLanguageException(String message) {
        super(message);
    }
    
    public DefaultLanguageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static DefaultLanguageException cannotDisable(String code) {
        return new DefaultLanguageException(String.format("Cannot disable default language '%s'", code));
    }
    
    public static DefaultLanguageException cannotDelete(String code) {
        return new DefaultLanguageException(String.format("Cannot delete default language '%s'", code));
    }
}