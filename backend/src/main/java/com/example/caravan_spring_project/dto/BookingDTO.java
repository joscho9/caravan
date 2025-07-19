package com.example.caravan_spring_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String caravanName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private Double totalPrice;
    private Double pricePerDay;
    private LocalDateTime createdAt;
} 