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
    
    public void sendContactNotification(ContactMessageDTO contactMessage) {
        logger.info("Starting email notification process for contact message from: {}", contactMessage.getEmail());
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("Neue Kontaktnachricht: " + contactMessage.getSubject());
        message.setText(createEmailContent(contactMessage));
        
        logger.debug("Email message prepared - To: {}, Subject: {}", adminEmail, message.getSubject());
        
        try {
            logger.info("Attempting to send email notification to admin: {}", adminEmail);
            mailSender.send(message);
            logger.info("Email notification sent successfully to admin: {}", adminEmail);
        } catch (Exception e) {
            logger.error("Failed to send email notification to admin: {}. Error: {}", adminEmail, e.getMessage(), e);
            // Log the error but don't throw it to avoid breaking the contact form
            System.err.println("Failed to send email notification: " + e.getMessage());
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