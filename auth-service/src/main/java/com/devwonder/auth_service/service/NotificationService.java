package com.devwonder.auth_service.service;

import com.devwonder.auth_service.dto.EmailNotificationEvent;
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

    public void sendResellerWelcomeEmail(String email, String name, String username) {
        try {
            EmailNotificationEvent event = EmailNotificationEvent.createResellerWelcomeEvent(email, name, username);
            kafkaTemplate.send(EMAIL_NOTIFICATION_TOPIC, username, event);
            log.info("Email notification event sent for reseller: {} to email: {}", username, email);
        } catch (Exception e) {
            log.error("Failed to send email notification event for reseller: {}", username, e);
        }
    }

    public void sendEmailNotification(EmailNotificationEvent event) {
        try {
            kafkaTemplate.send(EMAIL_NOTIFICATION_TOPIC, event.getUsername(), event);
            log.info("Email notification event sent: {} to {}", event.getEventType(), event.getTo());
        } catch (Exception e) {
            log.error("Failed to send email notification event: {}", event.getEventType(), e);
        }
    }
}