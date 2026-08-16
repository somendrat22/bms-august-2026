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
@Table(name = "live-event-mappings")
public class LiveEventMapping extends GlobalRecord{
    @ManyToOne
    private LiveEvent liveEvent;
    private Double basePrice;
    private String mappingName;
    private String capacity;
}
