package com.devwonder.notification_service.security;

import com.devwonder.notification_service.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Slf4j
@Component
public class WebSocketAuthorizationService {

    @EventListener
    public void handleSubscriptionEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        Principal principal = headerAccessor.getUser();
        
        if (destination == null || principal == null) {
            log.warn("Subscription attempt without destination or principal");
            throw new SecurityException("Authentication required");
        }
        
        String username = principal.getName();
        log.debug("User {} attempting to subscribe to: {}", username, destination);
        
        // Check user authorities/roles using SecurityUtil
        boolean isAdmin = SecurityUtil.hasRole(principal, "ADMIN");
        boolean isDealer = SecurityUtil.hasRole(principal, "DEALER");
        boolean isCustomer = SecurityUtil.hasRole(principal, "CUSTOMER");
        
        // Authorization rules
        if (destination.equals("/user/queue/notifications")) {
            if (!isAdmin) {
                log.warn("Access denied: user {} is not ADMIN for {}", username, destination);
                throw new SecurityException("Access denied: ADMIN role required");
            }
            log.info("Authorized ADMIN subscription: user {} to {}", username, destination);
        } else if (destination.equals("/user/queue/dealer-notifications")) {
            if (!isDealer && !isAdmin) {
                log.warn("Access denied: user {} is not DEALER/ADMIN for {}", username, destination);
                throw new SecurityException("Access denied: DEALER role required");
            }
            log.info("Authorized DEALER subscription: user {} to {}", username, destination);
        } else if (destination.equals("/user/queue/customer-notifications")) {
            if (!isCustomer && !isAdmin) {
                log.warn("Access denied: user {} is not CUSTOMER/ADMIN for {}", username, destination);
                throw new SecurityException("Access denied: CUSTOMER role required");
            }
            log.info("Authorized CUSTOMER subscription: user {} to {}", username, destination);
        } else if (destination.equals("/topic/notifications/broadcast") ||
                   destination.equals("/topic/notifications")) {
            // Public destinations - all authenticated users allowed
            log.info("Authorized public subscription: user {} to {}", username, destination);
        } else {
            log.warn("Unauthorized subscription attempt: user {} to {}", username, destination);
            throw new SecurityException("Access denied: Invalid subscription destination");
        }
    }
}