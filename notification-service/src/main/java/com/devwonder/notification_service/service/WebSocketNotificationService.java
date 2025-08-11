package com.devwonder.notification_service.service;

import com.devwonder.notification_service.dto.WebSocketNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendResellerRegistrationNotification(WebSocketNotificationEvent event) {
        // Send personal notification to the specific user
        sendUserNotification(event.getUserId(), event);
        
        // Also send broadcast notification to all connected users (for demo purposes)
        sendBroadcastNotification(event);
    }

    public void sendUserNotification(String username, WebSocketNotificationEvent event) {
        try {
            // Send to user-specific queue - Spring will automatically route to authenticated user
            messagingTemplate.convertAndSendToUser(username, "/queue/notifications", event);
            
            log.info("WebSocket notification sent successfully to user: {}", username);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification to user: {}", username, e);
            throw e;
        }
    }

    public void sendBroadcastNotification(WebSocketNotificationEvent event) {
        try {
            // Send to all authenticated users
            messagingTemplate.convertAndSend("/topic/notifications/broadcast", event);
            
            log.info("Broadcast WebSocket notification sent successfully: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to broadcast WebSocket notification: {}", event.getEventType(), e);
            throw e;
        }
    }
}