package com.example.caravan_spring_project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    @Column(nullable = false)
    private String caravanName;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private Double totalPrice;
    @Column(nullable = false)
    private Double pricePerDay;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Booking(Long id, String name, String email, String subject, String message, 
                   String caravanName, LocalDate startDate, LocalDate endDate, 
                   String location, Double totalPrice, Double pricePerDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.caravanName = caravanName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.totalPrice = totalPrice;
        this.pricePerDay = pricePerDay;
        // createdAt will be set by @PrePersist
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
} 