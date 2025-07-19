package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.CaravanDTO;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.service.CaravanDTOMapper;
import com.example.caravan_spring_project.service.CaravanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caravans")
public class CaravanController {

    private final CaravanService caravanService;
    private final CaravanDTOMapper caravanDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(CaravanController.class);

    public CaravanController(CaravanService caravanService, CaravanDTOMapper caravanDTOMapper) {
        this.caravanService = caravanService;
        this.caravanDTOMapper = caravanDTOMapper;
    }

    // GET /api/caravans - Get all caravans
    @GetMapping
    public List<CaravanDTO> getAllCaravans() {
        logger.info("GET /caravans called");
        List<Caravan> caravans = caravanService.getAllCaravans();
        logger.info("Caravans retrieved: {}", caravans.size());
        return caravans.stream()
                .map(caravanDTOMapper::mapCaravan)
                .toList();
    }

    // GET /api/caravans/{id} - Get caravan by ID
    @GetMapping("/{id}")
    public ResponseEntity<Caravan> getCaravan(@PathVariable UUID id) {
        logger.info("GET /caravans/{} called", id);
        Caravan caravan = caravanService.getCaravanById(id);
        if (caravan != null) {
            return ResponseEntity.ok(caravan);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // POST /api/caravans - Create new caravan
    @PostMapping
    public ResponseEntity<Caravan> createCaravan(@RequestBody Caravan caravan) {
        logger.info("POST /caravans called with data: {}", caravan);
        Caravan savedCaravan = caravanService.saveCaravan(caravan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaravan);
    }

    // POST /api/caravans/batch - Create multiple caravans
    @PostMapping("/batch")
    public ResponseEntity<List<Caravan>> createCaravans(@RequestBody List<Caravan> caravans) {
        logger.info("POST /caravans/batch called with {} caravans", caravans.size());
        List<Caravan> savedCaravans = caravanService.saveCaravans(caravans);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCaravans);
    }

    // PUT /api/caravans/{id} - Update caravan
    @PutMapping("/{id}")
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

    // DELETE /api/caravans/{id} - Delete caravan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaravan(@PathVariable UUID id) {
        logger.info("DELETE /caravans/{} called", id);
        caravanService.deleteCaravan(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/caravans/{id}/main-image - Set main image for caravan
    @PatchMapping("/{id}/main-image")
    public ResponseEntity<String> setMainImage(
            @PathVariable UUID id,
            @RequestParam String imageId){
        logger.info("PATCH /caravans/{}/main-image called", id);

        Caravan caravan = caravanService.getCaravanById(id);

        if(caravan == null){
            logger.warn("Caravan id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Caravan not found");
        }

        caravan.setMainImagePath(imageId);
        caravanService.saveCaravan(caravan);

        logger.info("MainImagePath set for Caravan: {}", caravan.getMainImagePath());
        return ResponseEntity.ok("Main image updated successfully");
    }
}
