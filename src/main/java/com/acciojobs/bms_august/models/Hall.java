package com.acciojobs.bms_august.models;

import com.acciojobs.bms_august.enums.HallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "halls")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hall extends GlobalRecord {
    @Column(nullable = false, unique = true)
    private String hallCode;

    @Column(nullable = false)
    private String hallName;

    @ManyToOne(optional = false)
    private Theater theater;

    private Integer floorNumber;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HallType hallType;
    // Facilities
    private boolean wheelchairAccessible;

    private boolean reclinerSeatsAvailable;

    private boolean dolbyAtmosSupported;

    private boolean threeDSupported;

    private boolean imaxSupported;

    // Maintenance / Operations
    @Column(nullable = false)
    private boolean active = true;

    private boolean underMaintenance = false;

    private String maintenanceRemarks;
}
