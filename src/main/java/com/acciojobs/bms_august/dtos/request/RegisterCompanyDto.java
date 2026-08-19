package com.acciojobs.bms_august.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyDto {

    private String companyName;

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
}
