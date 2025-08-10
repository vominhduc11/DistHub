package com.devwonder.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketNotificationEvent {
    
    private String userId;
    private String title;
    private String message;
    private String type;
    private String eventType;
    private Long timestamp;
    
    public static WebSocketNotificationEvent createResellerRegistrationEvent(String userId, String userName) {
        return new WebSocketNotificationEvent(
            userId,
            "Registration Successful",
            "Your reseller account has been successfully created. Welcome to DistHub!",
            "SUCCESS",
            "RESELLER_REGISTRATION",
            System.currentTimeMillis()
        );
    }
}