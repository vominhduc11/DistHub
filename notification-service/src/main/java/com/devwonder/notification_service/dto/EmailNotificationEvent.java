package com.devwonder.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationEvent {
    
    private String to;
    private String subject;
    private String templateName;
    private String recipientName;
    private String username;
    private String eventType;
    private Long timestamp;
    
    public static EmailNotificationEvent createResellerWelcomeEvent(String email, String name, String username) {
        return new EmailNotificationEvent(
            email,
            "Welcome to DistHub - Reseller Account Created",
            "reseller-welcome",
            name,
            username,
            "RESELLER_REGISTRATION",
            System.currentTimeMillis()
        );
    }
}