package com.example.caravan_spring_project.controller;

import com.example.caravan_spring_project.model.TrafficData;
import com.example.caravan_spring_project.service.TrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/traffic")
public class TrafficDataController {

    @Autowired
    private TrafficDataService trafficDataService;

    @GetMapping
    public List<TrafficData> getAllTrafficData() {
        return trafficDataService.getAllTrafficData();
    }

    @GetMapping("/{id}")
    public Optional<TrafficData> getTrafficDataById(@PathVariable Long id) {
        return trafficDataService.getTrafficDataById(id);
    }

    @PostMapping
    public TrafficData createTrafficData(@RequestBody TrafficData trafficData) {
        return trafficDataService.createTrafficData(trafficData);
    }

    @PutMapping("/{id}")
    public TrafficData updateTrafficData(@PathVariable Long id, @RequestBody TrafficData trafficDataDetails) {
        return trafficDataService.updateTrafficData(id, trafficDataDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteTrafficData(@PathVariable Long id) {
        trafficDataService.deleteTrafficData(id);
    }

    // Zusätzliche Endpunkte für spezifische Abfragen
    @GetMapping("/user/{userId}")
    public List<TrafficData> getTrafficDataByUserId(@PathVariable Long userId) {
        return trafficDataService.getTrafficDataByUserId(userId);
    }

    @GetMapping("/page/{pageUrl}")
    public List<TrafficData> getTrafficDataByPageUrl(@PathVariable String pageUrl) {
        return trafficDataService.getTrafficDataByPageUrl(pageUrl);
    }

    // Weitere Endpunkte entsprechend den Anforderungen deiner Anwendung
}