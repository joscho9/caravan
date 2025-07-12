package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import com.example.caravan_spring_project.service.ContactMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    
    @Autowired
    private ContactMessageService contactMessageService;
    
    // Submit a new contact message (public endpoint)
    @PostMapping
    public ResponseEntity<ContactMessageDTO> submitContactMessage(@RequestBody ContactMessageDTO contactMessageDTO) {
        logger.info("Received POST request for new contact message from: {} ({})", 
                   contactMessageDTO.getName(), contactMessageDTO.getEmail());
        
        try {
            ContactMessageDTO savedMessage = contactMessageService.saveContactMessage(contactMessageDTO);
            logger.info("Successfully processed contact message from: {} (ID: {})", 
                      contactMessageDTO.getEmail(), savedMessage.getId());
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            logger.error("Failed to process contact message from: {}. Error: {}", 
                        contactMessageDTO.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get all contact messages (admin endpoint)
    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        logger.info("Received GET request for all contact messages (admin endpoint)");
        
        try {
            List<ContactMessageDTO> messages = contactMessageService.getAllMessages();
            logger.info("Successfully retrieved {} contact messages for admin", messages.size());
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Failed to retrieve contact messages for admin. Error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 