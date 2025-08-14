package com.devwonder.auth_service.service;

import com.devwonder.auth_service.dto.EmailNotificationEvent;
import com.devwonder.auth_service.dto.WebSocketNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final String EMAIL_NOTIFICATION_TOPIC = "email-notifications";
    private static final String WEBSOCKET_NOTIFICATION_TOPIC = "websocket-notifications";

    public void sendResellerWelcomeEmail(String email, String name, String username) {
        try {
            EmailNotificationEvent event = EmailNotificationEvent.createResellerWelcomeEvent(email, name, username);
            kafkaTemplate.send(EMAIL_NOTIFICATION_TOPIC, username, event);
            log.info("Email notification event sent for reseller: {} to email: {}", username, email);
        } catch (Exception e) {
            log.error("Failed to send email notification event for reseller: {}", username, e);
        }
    }

    public void sendResellerRegistrationNotification(String userId, String userName) {
        try {
            WebSocketNotificationEvent event = WebSocketNotificationEvent.createResellerRegistrationEvent(userId, userName);
            kafkaTemplate.send(WEBSOCKET_NOTIFICATION_TOPIC, userId, event);
            log.info("WebSocket notification event sent for reseller registration: {} (userId: {})", userName, userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification event for reseller: {}", userName, e);
        }
    }

    public void sendCustomerRegistrationNotification(String userId, String userName) {
        try {
            WebSocketNotificationEvent event = WebSocketNotificationEvent.createCustomerRegistrationEvent(userId, userName);
            kafkaTemplate.send(WEBSOCKET_NOTIFICATION_TOPIC, userId, event);
            log.info("WebSocket notification event sent for customer registration: {} (userId: {})", userName, userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification event for customer: {}", userName, e);
        }
    }

}