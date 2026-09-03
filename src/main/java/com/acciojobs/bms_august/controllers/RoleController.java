package com.acciojobs.bms_august.controllers;

import com.acciojobs.bms_august.dtos.request.CreateRoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    //api/v1/role/create-role - Request Body (Contains role information)
    //CUSTOMER can not create a role, EVENT_COMPANY_EMPLOYEES, THEATRE_COMPANY_EMPLOYEE, INTERNAL_COMPANY_EMPLOYEE
    @PostMapping("/create-role")
    public ResponseEntity createRole(
            @RequestBody CreateRoleRequest createRoleRequest,
            @RequestHeader String token
            ){
        // So, to create role what all details we require -
        // roleName and what all operations is going to be performed by that role
        // Before creating role I want to know who is creating this role and the user who is creating this role is having access to create role ?
        // We need to verify that -> User is allowed to create role or not.
    }

}
