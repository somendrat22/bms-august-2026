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
@Entity
@Table(name = "movie-show-mappings")
public class MovieShowMapping extends GlobalRecord{
    @ManyToOne
    private MovieShow movieShow;
    private String mappingName;
    private String rowRange;
    private Double basePrice;
}
