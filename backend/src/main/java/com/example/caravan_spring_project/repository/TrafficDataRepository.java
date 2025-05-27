package com.example.caravan_spring_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.caravan_spring_project.model.TrafficData;

import java.util.List;


@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

    // Füge diese Methode hinzu:
    List<TrafficData> findByUserId(Long userId);

    // Weitere Methoden, falls benötigt:
    List<TrafficData> findByPageUrl(String pageUrl);
    // usw.

}