package com.acciojobs.bms_august.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "live_events")
public class LiveEvent extends BookableExperience {

    @ManyToOne(optional = false)
    private Company organizerCompany;

    private String eventType; // CONCERT, COMEDY, SPORTS, THEATRE

    private String venueName;

    private String artistNames;

    private String city;
}
