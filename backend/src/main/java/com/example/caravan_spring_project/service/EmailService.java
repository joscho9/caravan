package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${admin.email}")
    private String adminEmail;
    
    @Value("${admin.email.secondary}")
    private String adminEmailSecondary;
    
    public void sendContactNotification(ContactMessageDTO contactMessage) {
        logger.info("Starting email notification process for contact message from: {}", contactMessage.getEmail());
        
        // Send to primary admin email
        sendEmailToRecipient(contactMessage, adminEmail, "primary admin");
        
        // Send to secondary admin email
        sendEmailToRecipient(contactMessage, adminEmailSecondary, "secondary admin");
    }
    
    private void sendEmailToRecipient(ContactMessageDTO contactMessage, String recipientEmail, String recipientType) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Neue Kontaktnachricht: " + contactMessage.getSubject());
        message.setText(createEmailContent(contactMessage));
        
        logger.debug("Email message prepared - To: {} ({}), Subject: {}", recipientEmail, recipientType, message.getSubject());
        
        try {
            logger.info("Attempting to send email notification to {}: {}", recipientType, recipientEmail);
            mailSender.send(message);
            logger.info("Email notification sent successfully to {}: {}", recipientType, recipientEmail);
        } catch (Exception e) {
            logger.error("Failed to send email notification to {}: {}. Error: {}", recipientType, recipientEmail, e.getMessage(), e);
            // Log the error but don't throw it to avoid breaking the contact form
            System.err.println("Failed to send email notification to " + recipientType + ": " + e.getMessage());
        }
    }
    
    private String createEmailContent(ContactMessageDTO contactMessage) {
        StringBuilder content = new StringBuilder();
        content.append("Eine neue Kontaktnachricht wurde über das Kontaktformular gesendet.\n\n");
        content.append("Details:\n");
        content.append("Name: ").append(contactMessage.getName()).append("\n");
        content.append("E-Mail: ").append(contactMessage.getEmail()).append("\n");
        content.append("Betreff: ").append(contactMessage.getSubject()).append("\n");
        content.append("Nachricht:\n").append(contactMessage.getMessage()).append("\n\n");
        content.append("Zeitstempel: ").append(contactMessage.getCreatedAt()).append("\n");
        
        return content.toString();
    }
} 