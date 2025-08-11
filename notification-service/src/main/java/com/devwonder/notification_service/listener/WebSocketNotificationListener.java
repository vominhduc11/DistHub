package com.devwonder.notification_service.listener;

import com.devwonder.notification_service.dto.WebSocketNotificationEvent;
import com.devwonder.notification_service.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void handleWebSocketNotification(@Payload WebSocketNotificationEvent event) {
        log.info("Processing WebSocket notification: {} for user: {}", event.getEventType(), event.getUserId());
        
        try {
            switch (event.getEventType()) {
                case "RESELLER_REGISTRATION":
                    webSocketService.sendResellerRegistrationNotification(event);
                    break;
                default:
                    webSocketService.sendUserNotification(event.getUserId(), event);
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to process WebSocket notification for user: {}", event.getUserId(), e);
        }
    }
}