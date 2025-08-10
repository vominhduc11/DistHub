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
        try {
            String destination = "/topic/notifications/" + event.getUserId();
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("WebSocket notification sent successfully to user: {} at destination: {}", 
                    event.getUserId(), destination);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification to user: {}", event.getUserId(), e);
            throw e;
        }
    }

    public void sendNotification(WebSocketNotificationEvent event) {
        try {
            String destination = "/topic/notifications/" + event.getUserId();
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("WebSocket notification sent successfully to user: {} at destination: {}", 
                    event.getUserId(), destination);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification to user: {}", event.getUserId(), e);
            throw e;
        }
    }

    public void broadcastNotification(WebSocketNotificationEvent event) {
        try {
            String destination = "/topic/notifications/broadcast";
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("Broadcast WebSocket notification sent successfully: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to broadcast WebSocket notification: {}", event.getEventType(), e);
            throw e;
        }
    }
}