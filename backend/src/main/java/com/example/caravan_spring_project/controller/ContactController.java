package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.ContactMessageDTO;
import com.example.caravan_spring_project.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {
    
    @Autowired
    private ContactMessageService contactMessageService;
    
    // Submit a new contact message (public endpoint)
    @PostMapping
    public ResponseEntity<ContactMessageDTO> submitContactMessage(@RequestBody ContactMessageDTO contactMessageDTO) {
        try {
            ContactMessageDTO savedMessage = contactMessageService.saveContactMessage(contactMessageDTO);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get all contact messages (admin endpoint)
    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        try {
            List<ContactMessageDTO> messages = contactMessageService.getAllMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 