package com.acciojobs.bms_august.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "theaters")
public class Theater extends GlobalRecord{

    @Column(nullable = false, unique = true)
    private String theaterCode;

    @Column(nullable = false)
    private String theaterName;

    @ManyToOne
    private Company company; // Must be THEATER_COMPANY

    private String description;

    // Contact Details
    private String email;

    private String phoneNumber;

    // Address
    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String postalCode;

    // Location
    private Double latitude;

    private Double longitude;

    // Operations
    private String openingTime; // e.g. 09:00

    private String closingTime; // e.g. 23:59

    // Facilities
    private boolean parkingAvailable;

    private boolean foodCourtAvailable;

    private boolean wheelchairAccessible;

    private boolean dolbyAtmosSupported;

    private boolean imaxSupported;
}
