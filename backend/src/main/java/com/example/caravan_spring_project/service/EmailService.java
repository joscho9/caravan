package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import com.example.caravan_spring_project.dto.BookingDTO;
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
    
    @Value("${VITE_API_URL:http://localhost:8080}")
    private String baseUrl;
    
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
        }
    }
    
    private String createEmailContent(ContactMessageDTO contactMessage) {
        StringBuilder content = new StringBuilder();
        content.append("Eine neue Kontaktnachricht wurde über das Kontaktformular gesendet.\n\n");
        content.append("Details:\n");
        content.append("Name: ").append(contactMessage.getName()).append("\n");
        content.append("E-Mail: ").append(contactMessage.getEmail()).append("\n");
        content.append("Betreff: ").append(contactMessage.getSubject()).append("\n");
        content.append("Wohnwagen: ").append(contactMessage.getCaravanName()).append("\n");
        content.append("Nachricht:\n").append(contactMessage.getMessage()).append("\n\n");
        content.append("Zeitstempel: ").append(contactMessage.getCreatedAt()).append("\n");
        return content.toString();
    }

    public void sendBookingNotification(BookingDTO booking) {
        logger.info("Starting email notification process for booking from: {}", booking.getEmail());
        sendBookingEmailToRecipient(booking, adminEmail, "primary admin");
        sendBookingEmailToRecipient(booking, adminEmailSecondary, "secondary admin");
    }

    private void sendBookingEmailToRecipient(BookingDTO booking, String recipientEmail, String recipientType) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Neue Buchungsanfrage: " + booking.getCaravanName());
        message.setText(createBookingEmailContent(booking));

        logger.debug("Booking email message prepared - To: {} ({}), Subject: {}", recipientEmail, recipientType, message.getSubject());

        try {
            logger.info("Attempting to send booking email notification to {}: {}", recipientType, recipientEmail);
            mailSender.send(message);
            logger.info("Booking email notification sent successfully to {}: {}", recipientType, recipientEmail);
        } catch (Exception e) {
            logger.error("Failed to send booking email notification to {}: {}. Error: {}", recipientType, recipientEmail, e.getMessage(), e);
            System.err.println("Failed to send booking email notification to " + recipientType + ": " + e.getMessage());
        }
    }

    private String createBookingEmailContent(BookingDTO booking) {
        StringBuilder content = new StringBuilder();
        content.append("Eine neue Buchungsanfrage wurde ueber das Buchungsformular gesendet.\n\n");
        content.append("Details:\n");
        content.append("Name: ").append(booking.getName()).append("\n");
        content.append("E-Mail: ").append(booking.getEmail()).append("\n");
        content.append("Betreff: ").append(booking.getSubject()).append("\n");
        content.append("Wohnwagen: ").append(booking.getCaravanName()).append("\n");
        content.append("Startdatum: ").append(booking.getStartDate()).append("\n");
        content.append("Enddatum: ").append(booking.getEndDate()).append("\n");
        content.append("Standort: ").append(booking.getLocation()).append("\n");
        content.append("Gesamtpreis: ").append(booking.getTotalPrice()).append(" Euro\n");
        content.append("Nachricht:\n").append(booking.getMessage()).append("\n\n");
        content.append("Zeitstempel: ").append(booking.getCreatedAt()).append("\n\n");
        
        // Add confirmation/rejection links
        content.append("=== BUCHUNG BEARBEITEN ===\n\n");
        content.append("Buchung BESTAETIGEN (Termine werden automatisch gesperrt):\n");
        content.append(baseUrl).append("/bookings/confirm/").append(booking.getConfirmationToken()).append("\n\n");
        content.append("Buchung ABLEHNEN:\n");
        content.append(baseUrl).append("/bookings/reject/").append(booking.getConfirmationToken()).append("\n\n");
        content.append("WICHTIG: Jeder Link kann nur einmal verwendet werden!\n");
        
        return content.toString();
    }
} 