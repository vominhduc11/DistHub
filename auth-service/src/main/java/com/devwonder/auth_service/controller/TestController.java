package com.devwonder.auth_service.controller;

import com.devwonder.auth_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @PostMapping("/send-email")
    public String testSendEmail(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String username) {
        
        try {
            notificationService.sendResellerWelcomeEmail(email, name, username);
            return "Email notification sent successfully to " + email;
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
            return "Failed to send email: " + e.getMessage();
        }
    }
}