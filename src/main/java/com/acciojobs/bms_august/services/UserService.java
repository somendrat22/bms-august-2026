package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.dtos.request.UserLoginRequest;
import com.acciojobs.bms_august.exceptions.BMSUnauthorizedException;
import com.acciojobs.bms_august.models.*;
import com.acciojobs.bms_august.repositories.EmployeeRepository;
import com.acciojobs.bms_august.repositories.UserRepository;
import com.acciojobs.bms_august.transformers.UserTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Responsiblity of userservice is to handle user related operations
 */
@Service
@Slf4j
public class UserService {

    private RoleService roleService;
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public UserService(RoleService roleService,
                       UserRepository userRepository,
                       EmployeeRepository employeeRepository){
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }


    public Employee createCompanyAdminUser(Company company){
        // Q1 - I need to create a user with the help of company object which i am getting
        // So somehow i need to figure out what value inside my user object i will set.
        // So, Lets compare companyModel and userModel
        Employee sysAdmin = UserTransformer.transformCompanyToAdminUser(company);
        // we have created admin employee - but this user is not having any roles -
        // If it is a theaterCompanyAdmin so it should be having COMPANY_NAME_MAINT as the role
        // and this role can perform all the operations which is created for theatrecompany
        // To create adminRole for the user ->
        Role role = roleService.createAdminRoleByCompanyType(company.getCompanyName(), company.getCompanyType());
        sysAdmin.setRoles(List.of(role));
        // Save this user in the Employee table
        employeeRepository.save(sysAdmin);
        return sysAdmin;
    }

    public User authenticateUser(UserLoginRequest userLoginRequest){
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();
        // UserRepository -
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BMSUnauthorizedException("User email is invalid"));
        if(!user.getPasswordHash().equals(password)){
            throw new BMSUnauthorizedException("User password is invalid");
        }
        return user;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }




    public void saveOrUpdate(User user) {
        this.userRepository.save(user);
    }
}
