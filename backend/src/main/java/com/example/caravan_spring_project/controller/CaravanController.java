package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.dto.CaravanDTO;
import com.example.caravan_spring_project.model.Caravan;
import com.example.caravan_spring_project.service.CaravanDTOMapper;
import com.example.caravan_spring_project.service.CaravanService;
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

    public CaravanController(CaravanService caravanService, CaravanDTOMapper caravanDTOMapper) {
        this.caravanService = caravanService;
        this.caravanDTOMapper = caravanDTOMapper;
    }

    @GetMapping("/all")
    public List<CaravanDTO> getAllCaravans() {
        List<Caravan> caravans = caravanService.getAllCaravans();
        return caravans.stream()
                .map(caravanDTOMapper::mapCaravan)
                .toList();

    }

    @PostMapping("/add")
    public Caravan addCaravan(Caravan caravan) {
        return caravanService.saveCaravan(caravan);
    }

    @PostMapping("/add-list")
    public List<Caravan> addCaravans(@RequestBody List<Caravan> caravans) {
        return caravanService.saveCaravans(caravans);
    }

    @GetMapping("/{id}")
    public Caravan getCaravan(@PathVariable UUID id) {
        return caravanService.getCaravanById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Caravan> updateCaravan(@PathVariable UUID id, @RequestBody Caravan caravan){
        Caravan updatedCaravan = caravanService.updateCaravan(id, caravan);
        if (updatedCaravan != null) {
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

        if (!Objects.equals(token, "supersecret123")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nicht erlaubt.");
        }

        caravanService.resetDatabase();
        return ResponseEntity.ok("Datenbank wurde erfolgreich zurückgesetzt.");
    }
}
