package com.acciojobs.bms_august.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTheatreRequest {

    private String theaterName;


    private String description;


    private String email;

    private String phoneNumber;

    // Address

    private String addressLine1;

    private String addressLine2;


    private String city;


    private String state;


    private String country;

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
