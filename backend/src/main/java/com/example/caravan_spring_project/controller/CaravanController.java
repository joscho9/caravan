package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.CaravanDTO;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.service.CaravanDTOMapper;
import com.example.caravan_spring_project.service.CaravanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/caravans")
public class CaravanController {

    CaravanService caravanService;
    CaravanDTOMapper caravanDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(CaravanController.class);

    public CaravanController(CaravanService caravanService, CaravanDTOMapper caravanDTOMapper) {
        this.caravanService = caravanService;
        this.caravanDTOMapper = caravanDTOMapper;
    }

    @GetMapping("/all")
    public List<CaravanDTO> getAllCaravans() {
        logger.info("GET /caravans/all called");
        List<Caravan> caravans = caravanService.getAllCaravans();
        logger.info("Caravans retrieved: {}", caravans.size());
        return caravans.stream()
                .map(caravanDTOMapper::mapCaravan)
                .toList();
    }

    @PostMapping("/add")
    public Caravan addCaravan(Caravan caravan) {
        logger.info("POST /add called with data : {}", caravan);
        return caravanService.saveCaravan(caravan);
    }

    @PostMapping("/add-list")
    public List<Caravan> addCaravans(@RequestBody List<Caravan> caravans) {
        return caravanService.saveCaravans(caravans);
    }

    @GetMapping("/{id}")
    public Caravan getCaravan(@PathVariable UUID id) {
        logger.info("GET /caravans/{} called", id);
        return caravanService.getCaravanById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Caravan> updateCaravan(@PathVariable UUID id, @RequestBody Caravan caravan){
        logger.info("PUT /caravans/{} called", id);
        Caravan updatedCaravan = caravanService.updateCaravan(id, caravan);
        if (updatedCaravan != null) {
            logger.debug("Caravan updated: {}", updatedCaravan);
            return ResponseEntity.ok(updatedCaravan);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/delete")
    public void deleteCaravan(UUID id) {
        caravanService.deleteCaravan(id);
    }

    @DeleteMapping("/reset-database")
    public ResponseEntity<String> resetDatabase(@RequestHeader("X-Admin-Token") String token) {
        logger.info("DELETE /caravans/reset-database called");
        if (!Objects.equals(token, "supersecret123")) {
            logger.error("invalid token provided");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid token");
        }

        caravanService.resetDatabase();
        return ResponseEntity.ok("Datenbank wurde erfolgreich zurückgesetzt.");
    }

    @PostMapping("/set-main-image")
    public ResponseEntity<String> setMainImage(
            @RequestParam UUID id,
            @RequestParam String imageId){
        logger.info("POST /caravans/set-main-image called");

        Caravan caravan =  caravanService.getCaravanById(id);

        if(caravan == null){
            logger.warn("Caravan id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Caravan id " + id + " not found");
        }

        caravan.setMainImagePath(imageId);
        caravanService.saveCaravan(caravan);

        logger.info("MainImagePath set for Caravan: {}", caravan.getMainImagePath());
        return ResponseEntity.ok("MainImagePath set for Caravan");
    }

}
