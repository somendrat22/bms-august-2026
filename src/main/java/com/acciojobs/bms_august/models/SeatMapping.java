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
@Table(name = "seat-mappings")
public class SeatMapping extends GlobalRecord {
    private String row;
    @ManyToOne
    private Hall hall;
    private String seatRange;
    private String seatBreak;
}
