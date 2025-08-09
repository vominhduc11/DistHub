package com.devwonder.language_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private boolean success = false;
    private String message;
    private String error;
    private List<String> details;
    private LocalDateTime timestamp;
    private String path;
    
    public ErrorResponse(String message, String error, String path) {
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, String error, List<String> details, String path) {
        this.message = message;
        this.error = error;
        this.details = details;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}