package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.CaravanDTO;
import com.example.caravan_spring_project.dto.UserDTO;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.model.User;
import com.example.caravan_spring_project.model.CaravanImage;


import org.springframework.stereotype.Service;

import com.example.caravan_spring_project.dto.CaravanImageDTO;

import java.time.LocalDate;
import java.util.List;

@Service
public class CaravanDTOMapper {


    public CaravanDTO mapCaravan(Caravan caravan) {
        CaravanDTO dto = new CaravanDTO();
        dto.setId(caravan.getId());
        dto.setUserId(caravan.getUserId());
        dto.setMainImagePath(caravan.getMainImagePath());
        dto.setName(caravan.getName());
        dto.setAnzahlSchlafplaetze(caravan.getAnzahlSchlafplaetze());
        dto.setGesamtlaenge(caravan.getGesamtlaenge());
        dto.setNutzlaenge(caravan.getNutzlaenge());
        dto.setGesamtbreite(caravan.getGesamtbreite());
        dto.setLeergewicht(caravan.getLeergewicht());
        dto.setZulaessigesGesamtgewicht(caravan.getZulaessigesGesamtgewicht());
        dto.setKupplungstyp(caravan.getKupplungstyp());
        dto.setHoechstgeschwindigkeit(caravan.getHoechstgeschwindigkeit());
        dto.setUebergabepauschale(caravan.getUebergabepauschale());
        dto.setSummerPrice(caravan.getSummerPrice());
        dto.setWinterPrice(caravan.getWinterPrice());
        dto.setImages(mapCaravanImages(caravan.getImages()));
        dto.setUser(mapUser(caravan.getUser()));
        dto.setAusstattungsmerkmale(caravan.getAusstattungsmerkmale());
        dto.setUnavailableDates(mapUnavailableDates(caravan.getUnavailableDates()));
        dto.setDescription(caravan.getDescription());
        return dto;
    }

    private List<LocalDate> mapUnavailableDates(List<LocalDate> unavailableDates) {
        if(unavailableDates == null){
            return null;
        }

        return unavailableDates.stream()
                .filter(date -> date.isAfter(LocalDate.now()))
                .toList();
    }


    private List<CaravanImageDTO> mapCaravanImages(List<CaravanImage> images) {
        if(images == null){
            return null;
        }

        return images.stream()
                .map(this::mapCaravanImage)
                .toList();
    }

    private CaravanImageDTO mapCaravanImage(CaravanImage image){
        CaravanImageDTO dto = new CaravanImageDTO();
        dto.setId(image.getId());
        dto.setImagePath(image.getFilePath());
        dto.setDescription(image.getDescription());
        dto.setWidth(image.getWidth());
        dto.setHeight(image.getHeight());
        return dto;
    }

    private UserDTO mapUser(User user){
        if(user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}