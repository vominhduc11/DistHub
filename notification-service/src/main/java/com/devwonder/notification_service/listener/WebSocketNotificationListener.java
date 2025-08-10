package com.devwonder.notification_service.listener;

import com.devwonder.notification_service.dto.WebSocketNotificationEvent;
import com.devwonder.notification_service.service.WebSocketNotificationService;
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
public class WebSocketNotificationListener {

    private final WebSocketNotificationService webSocketService;

    @KafkaListener(
        topics = "websocket-notifications", 
        groupId = "notification-service-websocket-group",
        containerFactory = "webSocketNotificationKafkaListenerContainerFactory"
    )
    public void handleWebSocketNotification(
            @Payload WebSocketNotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("Received websocket notification event from topic: {}, partition: {}, offset: {}", topic, partition, offset);
        log.info("Event type: {}, user: {}", event.getEventType(), event.getUserId());
        
        try {
            switch (event.getEventType()) {
                case "RESELLER_REGISTRATION":
                    webSocketService.sendResellerRegistrationNotification(event);
                    break;
                default:
                    webSocketService.sendNotification(event);
                    break;
            }
            log.info("Successfully processed websocket notification for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to process websocket notification for user: {} - Error: {}", event.getUserId(), e.getMessage(), e);
        }
    }
}