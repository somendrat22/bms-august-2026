package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.dtos.response.LoginSuccessResponse;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.securites.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private JwtUtility jwtUtility;
    private UserService userService;

    @Autowired
    public AuthService(JwtUtility jwtUtility,
                       UserService userService){
        this.jwtUtility = jwtUtility;
        this.userService = userService;
    }

    public LoginSuccessResponse generateLoginSuccessToken(User user){
        String token =  this.jwtUtility.generateJwtToken(user);
        user.setLastLoginAt(LocalDateTime.now());
        userService.saveOrUpdate(user);
        return new LoginSuccessResponse(token);
    }

}
