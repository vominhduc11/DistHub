package com.devwonder.notification_service.service;

import com.devwonder.notification_service.dto.EmailNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendResellerWelcomeEmail(EmailNotificationEvent event) {
        sendActualEmail(event);
        log.info("Welcome email sent to: {}", event.getTo());
    }

    public void sendEmail(EmailNotificationEvent event) {
        sendActualEmail(event);
        log.info("Email sent to: {}", event.getTo());
    }

    private void sendActualEmail(EmailNotificationEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(event.getTo());
            message.setSubject(event.getSubject());
            message.setText(createEmailContent(event));
            
            emailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", event.getTo(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String createEmailContent(EmailNotificationEvent event) {
        return switch (event.getEventType()) {
            case "RESELLER_REGISTRATION" -> String.format(
                """
                Dear %s,
                
                Welcome to DistHub! Your reseller account has been successfully created.
                
                Username: %s
                Email: %s
                
                You can now log in and start using our platform.
                
                Best regards,
                DistHub Team
                
                ---
                This is an automated message. Please do not reply to this email.
                """,
                event.getRecipientName(),
                event.getUsername(),
                event.getTo()
            );
            default -> "Thank you for using DistHub!\n\nBest regards,\nDistHub Team";
        };
    }
}