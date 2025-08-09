package com.devwonder.notification_service.listener;

import com.devwonder.notification_service.dto.EmailNotificationEvent;
import com.devwonder.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
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
    public void handleEmailNotification(
            @Payload EmailNotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("Received email notification event from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        log.info("Event type: {}, recipient: {}", event.getEventType(), event.getTo());
        
        try {
            switch (event.getEventType()) {
                case "RESELLER_REGISTRATION":
                    emailService.sendResellerWelcomeEmail(event);
                    break;
                default:
                    emailService.sendEmail(event);
                    break;
            }
            log.info("Successfully processed email notification for: {}", event.getTo());
        } catch (Exception e) {
            log.error("Failed to process email notification for: {} - Error: {}", event.getTo(), e.getMessage(), e);
            // TODO: Implement retry logic or dead letter queue
        }
    }
}