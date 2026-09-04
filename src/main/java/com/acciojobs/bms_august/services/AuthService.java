package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.dtos.response.LoginSuccessResponse;
import com.acciojobs.bms_august.exceptions.BMSUnauthorizedException;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Operation;
import com.acciojobs.bms_august.models.Role;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.securites.JwtUtility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public Employee isEmployeeHavingAccess(String token, String operationName){
        String email = this.jwtUtility.extractAllClaims(token).get("email", String.class);
        // User service and get the user object.
        Employee user = userService.getEmployeeByEmail(email);
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            if(isRoleCanPerformOperation(role, operationName)){
                return user;
            }
        }
        throw new BMSUnauthorizedException("User is not allowed to perform this operation.");
    }


    public User isUserHavingAccess(String token, String operationName){
        // From the token we need to get the user details.
        String email = this.jwtUtility.extractAllClaims(token).get("email", String.class);
        // User service and get the user object.
        User user = userService.getUserByEmail(email);
        List<Role> roles = user.getRoles();
        for(Role role : roles){
            if(isRoleCanPerformOperation(role, operationName)){
                return user;
            }
        }
        throw new BMSUnauthorizedException("User is not allowed to perform this operation.");
    }

    private boolean isRoleCanPerformOperation(Role role, String operationName){
        List<Operation> operations = role.getOperations();
        for(Operation operation : operations){
            if(operation.getOperationName().equals(operationName)){
                return true;
            }
        }
        return false;
    }

}
