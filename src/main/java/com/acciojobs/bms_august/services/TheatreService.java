package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.dtos.request.RegisterCompanyDto;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Resposblity is to handle theatres related logic
 */
@Service
public class TheatreService {

    private CompanyService companyService;

    @Autowired
    public TheatreService(CompanyService companyService){
        this.companyService = companyService;
    }


    /**
     * registerTheatreCompany - Will Internally call CompanyService for the registration.
     * @param registerCompanyDto
     */
    public Company registerTheatreCompany(RegisterCompanyDto registerCompanyDto){
        // companyService -> registerCompany(dto, TheatreCompany)
        return companyService.registerCompany(registerCompanyDto, CompanyType.THEATER_COMPANY);
    }

}
