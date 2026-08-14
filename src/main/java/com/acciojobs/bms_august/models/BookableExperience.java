package com.acciojobs.bms_august.models;

import com.acciojobs.bms_august.enums.ExperienceStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "bookable_experiences")
public abstract class BookableExperience extends GlobalRecord{
    @Column(nullable = false)
    private String experienceCode; //BE-001

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private Integer minimumAge;

    private String language;

    private String category;

    private String posterUrl;

    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    private ExperienceStatus status; // DRAFT, PUBLISHED, CANCELLED, COMPLETED

    @Column(nullable = false)
    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
