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
        log.info("Sending reseller welcome email to: {} for user: {}", event.getTo(), event.getUsername());
        sendActualEmail(event);
        log.info("Successfully sent welcome email to reseller: {} ({})", event.getRecipientName(), event.getTo());
    }

    public void sendEmail(EmailNotificationEvent event) {
        log.info("Sending email: {} to: {}", event.getSubject(), event.getTo());
        sendActualEmail(event);
        log.info("Successfully sent email: {} to: {}", event.getEventType(), event.getTo());
    }

    private void sendActualEmail(EmailNotificationEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(event.getTo());
            message.setSubject(event.getSubject());
            message.setText(createEmailContent(event));
            
            emailSender.send(message);
            log.info("Email sent successfully to: {}", event.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", event.getTo(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String createEmailContent(EmailNotificationEvent event) {
        if ("RESELLER_REGISTRATION".equals(event.getEventType())) {
            return String.format(
                "Dear %s,\n\n" +
                "Welcome to DistHub! Your reseller account has been successfully created.\n\n" +
                "Username: %s\n" +
                "Email: %s\n\n" +
                "You can now log in and start using our platform.\n\n" +
                "Best regards,\n" +
                "DistHub Team\n\n" +
                "---\n" +
                "This is an automated message. Please do not reply to this email.",
                event.getRecipientName(),
                event.getUsername(),
                event.getTo()
            );
        }
        
        return "Thank you for using DistHub!\n\nBest regards,\nDistHub Team";
    }

}