package com.example.caravan_spring_project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "caravan_image")
@Data
public class CaravanImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    private String description;

    private Integer width;
    private Integer height;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "caravan_id")
    private Caravan caravan;
}