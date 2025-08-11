package com.devwonder.notification_service.listener;

import com.devwonder.notification_service.dto.EmailNotificationEvent;
import com.devwonder.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private final EmailService emailService;

    @KafkaListener(
        topics = "email-notifications", 
        groupId = "notification-service-group",
        containerFactory = "emailNotificationKafkaListenerContainerFactory"
    )
    public void handleEmailNotification(@Payload EmailNotificationEvent event) {
        log.info("Processing email notification: {} for {}", event.getEventType(), event.getTo());
        
        try {
            switch (event.getEventType()) {
                case "RESELLER_REGISTRATION":
                    emailService.sendResellerWelcomeEmail(event);
                    break;
                default:
                    emailService.sendEmail(event);
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to process email notification for: {}", event.getTo(), e);
            throw e;
        }
    }
}