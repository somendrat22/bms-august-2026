package com.acciojobs.bms_august.controllers;

import com.acciojobs.bms_august.constants.LoggerConstant;
import com.acciojobs.bms_august.dtos.request.CreateTheatreRequest;
import com.acciojobs.bms_august.dtos.request.RegisterCompanyDto;
import com.acciojobs.bms_august.enums.OperationType;
import com.acciojobs.bms_august.models.Company;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Theater;
import com.acciojobs.bms_august.services.AuthService;
import com.acciojobs.bms_august.services.TheatreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Theatre companies
 * event companies
 * internal
 * customer
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/theatre")
public class TheatreController {

    private TheatreService theatreService;
    private AuthService authService;

    @Autowired
    public TheatreController(TheatreService theatreService,
                             AuthService authService){
        this.theatreService = theatreService;
        this.authService = authService;
    }

    /**
     * This method will recieve request from frontend/ui for the registration of theatre
     * Internally inside it - It will call TheatreService to perform the task.
     * @param registerCompanyDto
     * @return
     */
    @PostMapping("/company/register")
    public ResponseEntity registerTheatreCompany(
            @RequestBody RegisterCompanyDto registerCompanyDto
            ){
        log.info(String.format(LoggerConstant.REQUEST_RECEIVED_MESSAGE, "registerTheatreCompany", registerCompanyDto.toString()));
        // TheatreService
        Company theatreCompany = theatreService.registerTheatreCompany(registerCompanyDto);
        return new ResponseEntity<>(theatreCompany, HttpStatus.CREATED);
    }

    @PostMapping("/register-theatre")
    public ResponseEntity registerTheatre(
            @RequestBody CreateTheatreRequest createTheatreRequest,
            @RequestHeader String bmsToken
            ){
        Employee creatorEmployee = authService.isEmployeeHavingAccess(bmsToken, OperationType.CREATE_THEATRE.name());
        Theater theater = theatreService.registerTheatre(creatorEmployee, createTheatreRequest);
        return new ResponseEntity(theater, HttpStatus.CREATED);
    }

}
