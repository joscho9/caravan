package com.example.caravan_spring_project.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CaravanImageDTO {

    private Long id;
    private String imagePath;
    private String description;
    private Integer width;
    private Integer height;
}

