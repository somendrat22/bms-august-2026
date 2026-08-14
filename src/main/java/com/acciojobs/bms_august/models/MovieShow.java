package com.acciojobs.bms_august.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "movie-shows")
@Entity
public class MovieShow extends BookableExperience {
    @ManyToOne(optional = false)
    private Movie movie;
    
    private Long startTimeInSeconds;

    private Long endTimeInSeconds;

    @ManyToOne(optional = false)
    private Hall hall;

    private String screenFormat; // 2D, 3D, IMAX, 4DX

    private Integer intermissionMinutes;
}
