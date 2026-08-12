package com.acciojobs.bms_august.models;

import com.acciojobs.bms_august.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class Customer extends User{

    @Column(nullable = false, unique = true)
    private String customerCode;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String preferredLanguage;

    private String preferredCity;

    private String favoriteGenre;

    @Column(nullable = false)
    private int loyaltyPoints = 0;

    @Column(nullable = false)
    private boolean active = true;
}
