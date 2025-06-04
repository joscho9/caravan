package com.example.caravan_spring_project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "caravans")
@Getter
@Setter
public class Caravan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "main_image_path", nullable = true)
    private String mainImagePath;

    @Column(name = "wohnwagentyp", nullable = false)
    private String wohnwagentyp;

    @Column(name = "anzahl_schlafplaetze", nullable = false)
    private String anzahlSchlafplaetze;

    @Column(name = "gesamtlaenge", nullable = false)
    private String gesamtlaenge;

    @Column(name = "nutzlaenge", nullable = false)
    private String nutzlaenge;

    @Column(name = "gesamtbreite", nullable = false)
    private String gesamtbreite;

    @Column(name = "leergewicht", nullable = false)
    private String leergewicht;

    @Column(name = "zulaessiges_gesamtgewicht", nullable = false)
    private String zulaessigesGesamtgewicht;

    @Column(name = "kupplungstyp", nullable = false)
    private String kupplungstyp;

    @Column(name = "hoechstgeschwindigkeit", nullable = false)
    private String hoechstgeschwindigkeit;

    @Column(name = "uebergabepauschale", nullable = false)
    private String uebergabepauschale;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "caravan")
    private List<CaravanImage> images;

    @ElementCollection
    @CollectionTable(name = "caravan_unavailable_dates", joinColumns = @JoinColumn(name = "caravan_id"))
    @Column(name = "date")
    private List<LocalDate> unavailableDates;

    @ElementCollection
    @CollectionTable(name = "caravan_features", joinColumns = @JoinColumn(name = "caravan_id"))
    @Column(name = "feature")
    private List<String> ausstattungsmerkmale;

    @Column(name = "description")
    @Lob
    private String description;
}