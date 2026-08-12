package com.acciojobs.bms_august.models;

import com.acciojobs.bms_august.enums.CompanyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends GlobalRecord {
    @Column(nullable = false, unique = true)
    private String companyCode;

    @Column(nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    private String legalName;

    private String registrationNumber;

    private String gstNumber;

    private String panNumber;

    private String email;

    private String phoneNumber;

    private String website;

    // Address
    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    // Branding
    private String logoUrl;

    // Status
    @Column(nullable = false)
    private boolean active = true;
}
