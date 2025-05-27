package com.example.caravan_spring_project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CaravanDTO {

        private UUID id;
        private Long userId;
        private String imagePath;
        private String wohnwagentyp;
        private String anzahlSchlafplaetze;
        private String gesamtlaenge;
        private String nutzlaenge;
        private String gesamtbreite;
        private String leergewicht;
        private String zulaessigesGesamtgewicht;
        private String kupplungstyp;
        private String hoechstgeschwindigkeit;
        private String uebergabepauschale;
        private List<CaravanImageDTO> images; // DTOs für CaravanImages
        private UserDTO user; // DTOs für User
        private List<String> ausstattungsmerkmale;
        private List<LocalDate> unavailableDates; // DTOs für CaravanUnavailableDate
        private String description;
}


