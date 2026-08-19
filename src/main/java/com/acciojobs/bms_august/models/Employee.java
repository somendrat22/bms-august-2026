package com.acciojobs.bms_august.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends User{
    @ManyToOne
    private Company company;

    @Column(nullable = false, unique = true)
    private String employeeCode;

    private String designation;

    private String department;

    private String workLocation;

    @ManyToOne
    private Employee manager;

    @Column(nullable = false)
    private boolean active = true;
}
