package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import com.example.caravan_spring_project.model.ContactMessage;
import com.example.caravan_spring_project.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {
    
    @Autowired
    private ContactMessageRepository contactMessageRepository;
    
    @Autowired
    private EmailService emailService;
    
    // Save a new contact message
    public ContactMessageDTO saveContactMessage(ContactMessageDTO dto) {
        ContactMessage message = new ContactMessage(
            dto.getName(),
            dto.getEmail(),
            dto.getSubject(),
            dto.getMessage()
        );
        
        ContactMessage savedMessage = contactMessageRepository.save(message);
        ContactMessageDTO savedDTO = convertToDTO(savedMessage);
        
        // Send email notification
        emailService.sendContactNotification(savedDTO);
        
        return savedDTO;
    }
    
    // Get all messages (for admin)
    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Get message by ID
    public ContactMessageDTO getMessageById(Long messageId) {
        ContactMessage message = contactMessageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));
        
        return convertToDTO(message);
    }
    
    // Delete message
    public void deleteMessage(Long messageId) {
        contactMessageRepository.deleteById(messageId);
    }
    
    // Convert entity to DTO
    private ContactMessageDTO convertToDTO(ContactMessage message) {
        return new ContactMessageDTO(
            message.getId(),
            message.getName(),
            message.getEmail(),
            message.getSubject(),
            message.getMessage(),
            message.getCreatedAt()
        );
    }
} 