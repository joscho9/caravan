package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.model.TrafficData;
import com.example.caravan_spring_project.repository.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TrafficDataService {

    @Autowired
    private TrafficDataRepository trafficDataRepository;

    public List<TrafficData> getAllTrafficData() {
        return trafficDataRepository.findAll();
    }

    public Optional<TrafficData> getTrafficDataById(Long id) {
        return trafficDataRepository.findById(id);
    }

    public TrafficData createTrafficData(TrafficData trafficData) {
        return trafficDataRepository.save(trafficData);
    }

    public TrafficData updateTrafficData(Long id, TrafficData trafficDataDetails) {
        Optional<TrafficData> trafficDataOptional = trafficDataRepository.findById(id);
        if (trafficDataOptional.isPresent()) {
            TrafficData trafficData = trafficDataOptional.get();
            // Aktualisiere die Felder von trafficData mit den Werten von trafficDataDetails
            // Beispiel:
            trafficData.setSessionId(trafficDataDetails.getSessionId());
            trafficData.setUserId(trafficDataDetails.getUserId());
            trafficData.setVisitTimestamp(trafficDataDetails.getVisitTimestamp());
            trafficData.setPageUrl(trafficDataDetails.getPageUrl());
            // ... andere Felder ...
            return trafficDataRepository.save(trafficData);
        } else {
            return null; // oder werfe eine Exception
        }
    }

    public void deleteTrafficData(Long id) {
        trafficDataRepository.deleteById(id);
    }

    // Zusätzliche Methoden für spezifische Abfragen
    public List<TrafficData> getTrafficDataByUserId(Long userId) {
        // Überprüfung, ob nutzer bereits registriert ist.
        if (userId == null) {
            return null;
        }
        return trafficDataRepository.findByUserId(userId);
    }

    public List<TrafficData> getTrafficDataByPageUrl(String pageUrl) {
        return trafficDataRepository.findByPageUrl(pageUrl);
    }
}