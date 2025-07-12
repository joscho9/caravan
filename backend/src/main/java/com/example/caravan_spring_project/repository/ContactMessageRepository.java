package com.example.caravan_spring_project.repository;

import com.example.caravan_spring_project.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    
    // Find all messages ordered by creation date (newest first)
    List<ContactMessage> findAllByOrderByCreatedAtDesc();
    
    // Find messages by email
    List<ContactMessage> findByEmailOrderByCreatedAtDesc(String email);
} 