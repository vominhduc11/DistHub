package com.devwonder.notification_service.security;

import com.devwonder.notification_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            
            if (token == null || !token.startsWith("Bearer ")) {
                log.warn("WebSocket connection attempt without valid Authorization header");
                throw new SecurityException("Missing or invalid Authorization header");
            }
            
            token = token.substring(7); // Remove "Bearer " prefix
            
            if (!jwtUtil.validateToken(token)) {
                log.warn("WebSocket connection attempt with invalid JWT token");
                throw new SecurityException("Invalid JWT token");
            }
            
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);
            
            if (username == null) {
                log.warn("WebSocket connection attempt with token missing username");
                throw new SecurityException("Token missing username");
            }
            
            if (roles == null || roles.isEmpty()) {
                log.warn("WebSocket connection attempt with token missing roles");
                throw new SecurityException("Token missing roles");
            }
            
            // Create authentication with multiple roles
            List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            // Set principal in accessor
            accessor.setUser(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.info("WebSocket authenticated user: {} with roles: {}", username, roles);
        }
        
        return message;
    }
}