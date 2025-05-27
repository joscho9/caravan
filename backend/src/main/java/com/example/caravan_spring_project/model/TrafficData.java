package com.example.caravan_spring_project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "traffic_data")
@Getter
@Setter
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "visit_timestamp", nullable = false)
    private LocalDateTime visitTimestamp;

    @Column(name = "page_url", nullable = false)
    private String pageUrl;

    @Column(name = "referer_url")
    private String refererUrl;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "browser")
    private String browser;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "duration")
    private Long duration; // Dauer des Seitenbesuchs in Sekunden

    @Column(name = "event_type")
    private String eventType; // Art des Ereignisses (z. B. Seitenaufruf, Klick, Formularübermittlung)

    @Column(name = "event_details")
    private String eventDetails; // Zusätzliche Details zum Ereignis (z. B. angeklicktes Element, übermittelte Formulardaten)
}