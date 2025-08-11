package com.devwonder.notification_service.controller;

import com.devwonder.notification_service.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-broadcast-notification")
    public void sendBroadcastNotification(@Payload String message, Principal principal) {
        // Check ADMIN role
        if (!SecurityUtil.hasRole(principal, "ADMIN")) {
            log.warn("Access denied: user {} is not ADMIN for broadcast", principal.getName());
            throw new SecurityException("Access denied: ADMIN role required");
        }
        
        log.info("ADMIN {} sending broadcast notification: {}", principal.getName(), message);
        
        // Send to all subscribers of broadcast topic
        messagingTemplate.convertAndSend("/topic/notifications/broadcast", 
            "Broadcast from " + principal.getName() + ": " + message);
    }

    @MessageMapping("/send-user-notification")
    public void sendUserNotification(@Payload UserNotificationRequest request, Principal principal) {
        // Check ADMIN role
        if (!SecurityUtil.hasRole(principal, "ADMIN")) {
            log.warn("Access denied: user {} is not ADMIN for user notification", principal.getName());
            throw new SecurityException("Access denied: ADMIN role required");
        }
        
        log.info("ADMIN {} sending notification to user {}: {}", 
            principal.getName(), request.getTargetUser(), request.getMessage());
        
        // Send to specific user
        messagingTemplate.convertAndSendToUser(request.getTargetUser(), 
            "/queue/notifications", 
            "Message from " + principal.getName() + ": " + request.getMessage());
    }

    public static class UserNotificationRequest {
        private String targetUser;
        private String message;
        
        public String getTargetUser() { return targetUser; }
        public void setTargetUser(String targetUser) { this.targetUser = targetUser; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

}