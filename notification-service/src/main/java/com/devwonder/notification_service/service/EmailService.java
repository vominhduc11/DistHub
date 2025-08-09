package com.devwonder.notification_service.service;

import com.devwonder.notification_service.dto.EmailNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendResellerWelcomeEmail(EmailNotificationEvent event) {
        log.info("Sending reseller welcome email to: {} for user: {}", event.getTo(), event.getUsername());
        
        try {
            // TODO: Implement actual email sending logic
            // For now, we'll just simulate email sending
            simulateEmailSending(event);
            
            log.info("Successfully sent welcome email to reseller: {} ({})", event.getRecipientName(), event.getTo());
        } catch (Exception e) {
            log.error("Failed to send welcome email to reseller: {} ({})", event.getRecipientName(), event.getTo(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    public void sendEmail(EmailNotificationEvent event) {
        log.info("Sending email: {} to: {}", event.getSubject(), event.getTo());
        
        try {
            simulateEmailSending(event);
            log.info("Successfully sent email: {} to: {}", event.getEventType(), event.getTo());
        } catch (Exception e) {
            log.error("Failed to send email: {} to: {}", event.getEventType(), event.getTo(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private void simulateEmailSending(EmailNotificationEvent event) {
        // Simulate email processing time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Log email content simulation
        log.info("📧 EMAIL SIMULATION 📧");
        log.info("To: {}", event.getTo());
        log.info("Subject: {}", event.getSubject());
        log.info("Template: {}", event.getTemplateName());
        log.info("Recipient: {}", event.getRecipientName());
        log.info("Username: {}", event.getUsername());
        log.info("Event Type: {}", event.getEventType());
        log.info("Timestamp: {}", event.getTimestamp());
        
        if ("RESELLER_REGISTRATION".equals(event.getEventType())) {
            log.info("📧 Email Content:");
            log.info("Dear {},", event.getRecipientName());
            log.info("Welcome to DistHub! Your reseller account has been successfully created.");
            log.info("Username: {}", event.getUsername());
            log.info("You can now log in and start using our platform.");
            log.info("Best regards,");
            log.info("DistHub Team");
        }
        
        log.info("📧 END EMAIL SIMULATION 📧");
    }
}