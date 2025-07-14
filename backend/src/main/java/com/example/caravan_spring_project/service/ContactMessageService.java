package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import com.example.caravan_spring_project.model.ContactMessage;
import com.example.caravan_spring_project.repository.ContactMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ContactMessageService.class);
    
    @Autowired
    private ContactMessageRepository contactMessageRepository;
    
    @Autowired
    private EmailService emailService;
    
    // Save a new contact message
    public ContactMessageDTO saveContactMessage(ContactMessageDTO dto) {
        logger.info("Received new contact message from: {} ({})", dto.getName(), dto.getEmail());
        logger.debug("Contact message details - Subject: {}, Message length: {} characters", 
                   dto.getSubject(), dto.getMessage().length());
        
        ContactMessage message = new ContactMessage(
            dto.getName(),
            dto.getEmail(),
            dto.getSubject(),
            dto.getMessage(),
            dto.getCaravanName()
        );
        
        logger.debug("Saving contact message to database...");
        ContactMessage savedMessage = contactMessageRepository.save(message);
        ContactMessageDTO savedDTO = convertToDTO(savedMessage);
        logger.info("Contact message saved successfully with ID: {}", savedMessage.getId());
        
        // Send email notification
        logger.info("Initiating email notification for contact message ID: {}", savedMessage.getId());
        emailService.sendContactNotification(savedDTO);
        
        logger.info("Contact message processing completed successfully for: {}", dto.getEmail());
        return savedDTO;
    }
    
    // Get all messages (for admin)
    public List<ContactMessageDTO> getAllMessages() {
        logger.debug("Retrieving all contact messages for admin view");
        List<ContactMessageDTO> messages = contactMessageRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        logger.info("Retrieved {} contact messages for admin view", messages.size());
        return messages;
    }
    
    // Get message by ID
    public ContactMessageDTO getMessageById(Long messageId) {
        logger.debug("Retrieving contact message with ID: {}", messageId);
        ContactMessage message = contactMessageRepository.findById(messageId)
            .orElseThrow(() -> {
                logger.error("Contact message not found with ID: {}", messageId);
                return new RuntimeException("Message not found");
            });
        
        logger.debug("Successfully retrieved contact message with ID: {}", messageId);
        return convertToDTO(message);
    }
    
    // Delete message
    public void deleteMessage(Long messageId) {
        logger.info("Deleting contact message with ID: {}", messageId);
        contactMessageRepository.deleteById(messageId);
        logger.info("Successfully deleted contact message with ID: {}", messageId);
    }
    
    // Convert entity to DTO
    private ContactMessageDTO convertToDTO(ContactMessage message) {
        return new ContactMessageDTO(
            message.getId(),
            message.getName(),
            message.getEmail(),
            message.getSubject(),
            message.getMessage(),
            message.getCreatedAt(),
            message.getCaravanName()
        );
    }
} 