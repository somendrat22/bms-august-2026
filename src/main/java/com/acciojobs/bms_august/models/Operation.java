package com.acciojobs.bms_august.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "operations")
public class Operation extends GlobalRecord{
    private String operationName;
    private String operationCategory;
}
