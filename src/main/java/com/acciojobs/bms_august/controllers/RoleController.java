package com.acciojobs.bms_august.controllers;

import com.acciojobs.bms_august.dtos.request.CreateRoleRequest;
import com.acciojobs.bms_august.enums.OperationType;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Role;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.services.AuthService;
import com.acciojobs.bms_august.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    public AuthService authService;
    public RoleService roleService;

    @Autowired
    public RoleController(AuthService authService,
                          RoleService roleService){
        this.authService = authService;
        this.roleService = roleService;
    }

    //api/v1/role/create-role - Request Body (Contains role information)
    //CUSTOMER can not create a role, EVENT_COMPANY_EMPLOYEES, THEATRE_COMPANY_EMPLOYEE, INTERNAL_COMPANY_EMPLOYEE
    @PostMapping("/create-role")
    public ResponseEntity createRole(
            @RequestBody CreateRoleRequest createRoleRequest,
            @RequestHeader String bmsToken
            ){
        // So, to create role what all details we require -
        // roleName and what all operations is going to be performed by that role
        // Before creating role I want to know who is creating this role and the user who is creating this role is having access to create role ?
        // We need to verify that -> User is allowed to create role or not.
        Employee employee = authService.isEmployeeHavingAccess(bmsToken, OperationType.CREATE_ROLE.name());
        Role role = roleService.createRole(employee, createRoleRequest);
        return new ResponseEntity(role, HttpStatus.CREATED);
    }

}
