package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.CaravanSpringProjectApplication;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.model.CaravanImage;
import com.example.caravan_spring_project.repository.CaravanImageRepository;
import com.example.caravan_spring_project.repository.CaravanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CaravanService {

    private static final Logger log = LoggerFactory.getLogger(CaravanService.class);

    private final CaravanRepository caravanRepository;
    private final CaravanImageRepository caravanImageRepository;

    public CaravanService(CaravanRepository caravanRepository, CaravanImageRepository caravanImageRepository) {
        this.caravanRepository = caravanRepository;
        this.caravanImageRepository = caravanImageRepository;
    }

    public List<Caravan> getAllCaravans() {
        return caravanRepository.findAll();
    }

    public Caravan getCaravanById(UUID id) {
        log.info("CaravanService.getCaravanById: " + id);
        Caravan caravan = caravanRepository.findById(id).orElse(null);
        log.info("CaravanService.getCaravanById: " + caravan);
        return caravan;

    }

    public Caravan saveCaravan(Caravan caravan) {
        return caravanRepository.save(caravan);
    }

    public Caravan updateCaravan(UUID id, Caravan caravan) {
        if (caravanRepository.existsById(id)) {
            caravan.setId(id);
            return caravanRepository.save(caravan);
        } else {
            return null;
        }
    }

    public void deleteCaravan(UUID id) {
        caravanRepository.deleteById(id);
    }


    public List<Caravan> saveCaravans(List<Caravan> caravans) {
        return caravanRepository.saveAll(caravans);
    }

    public void resetDatabase() {
        caravanImageRepository.deleteAll();
        caravanRepository.deleteAll();
    }


    public void addCaravanImage(Caravan caravan, String filePath, String description, Integer width, Integer height) {
        CaravanImage image = new CaravanImage();
        image.setCaravan(caravan);
        image.setFilePath(filePath);
        image.setDescription(description);
        image.setWidth(width);
        image.setHeight(height);
        caravanImageRepository.save(image);
    }
}
