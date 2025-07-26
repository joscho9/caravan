package com.example.caravan_spring_project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CaravanDTO {

        private UUID id;
        private Long userId;
        private String mainImagePath;
        private String name;
        private String anzahlSchlafplaetze;
        private String gesamtlaenge;
        private String nutzlaenge;
        private String gesamtbreite;
        private String leergewicht;
        private String zulaessigesGesamtgewicht;
        private String kupplungstyp;
        private String hoechstgeschwindigkeit;
        private Double handoverFee;
        private Double summerPrice;
        private Double winterPrice;
        private List<CaravanImageDTO> images; // DTOs für CaravanImages
        
        // Utility method to get price for a specific date
        public Double getPriceForDate(java.time.LocalDate date) {
            int month = date.getMonthValue();
            // Summer season: April 1st to August 31st (04-01 to 08-31)
            // Winter season: September 1st to March 31st (09-01 to 03-31)
            if (month >= 4 && month <= 8) {
                return this.summerPrice;
            } else {
                return this.winterPrice;
            }
        }
        private UserDTO user; // DTOs für User
        private List<String> ausstattungsmerkmale;
        private List<LocalDate> unavailableDates; // DTOs für CaravanUnavailableDate
        private String description;
}


