package com.acciojobs.bms_august.dtos.request;

import com.acciojobs.bms_august.enums.MovieLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequest {

    private String title;

    private String originalTitle;

    private String synopsis;

    private MovieLanguage language;

    private String genre; // Action, Comedy, Drama

    private Integer durationMinutes;

    private LocalDate releaseDate;

    private String censorCertificate; // U, UA, A

    private String director;

    private String producer;

    private String castMembers; // comma separated or JSON later

    private String musicDirector;

    private String productionHouse;

    private String country;

    private String trailerUrl;

    private String posterUrl;

    private String bannerUrl;

    private Double imdbRating;
}
