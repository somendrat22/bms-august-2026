package com.acciojobs.bms_august.controllers;

import com.acciojobs.bms_august.dtos.request.UserLoginRequest;
import com.acciojobs.bms_august.dtos.response.LoginSuccessResponse;
import com.acciojobs.bms_august.exceptions.BMSUnauthorizedException;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.services.AuthService;
import com.acciojobs.bms_august.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;
    private AuthService authService;

    @Autowired
    public UserController(UserService userService,
                          AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity authenticateUser(
            @RequestBody UserLoginRequest userLoginRequest
            ){
        User user = userService.authenticateUser(userLoginRequest);
        LoginSuccessResponse loginSuccessResponse = authService.generateLoginSuccessToken(user);
        return new ResponseEntity(loginSuccessResponse, HttpStatus.OK);
    }

}
