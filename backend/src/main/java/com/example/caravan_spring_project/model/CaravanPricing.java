package com.example.caravan_spring_project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "caravan_pricing")
@Data
public class CaravanPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price_per_day", nullable = false)
    private Integer pricePerDay;

    @ManyToOne
    @JoinColumn(name = "caravan_id")
    private Caravan caravan;
}