package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.NotificationTemplateConfig;
import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.dtos.request.InviteEmployeeRequest;
import com.acciojobs.bms_august.dtos.request.UserLoginRequest;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.enums.UserType;
import com.acciojobs.bms_august.exceptions.BMSUnauthorizedException;
import com.acciojobs.bms_august.models.*;
import com.acciojobs.bms_august.repositories.EmployeeRepository;
import com.acciojobs.bms_august.repositories.UserRepository;
import com.acciojobs.bms_august.transformers.UserTransformer;
import com.acciojobs.bms_august.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * Responsiblity of userservice is to handle user related operations
 */
@Service
@Slf4j
public class UserService {

    private RoleService roleService;
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private ExecutorService executorService;
    private NotificationService notificationService;

    @Autowired
    public UserService(RoleService roleService,
                       UserRepository userRepository,
                       EmployeeRepository employeeRepository,
                       ExecutorService executorService,
                       NotificationService notificationService){
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.executorService = executorService;
        this.notificationService = notificationService;
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

    public Employee getEmployeeByEmail(String email){
        return employeeRepository.findByEmail(email).orElse(null);
    }

    public Employee inviteEmployee(Employee inviterEmp, InviteEmployeeRequest inviteEmployeeRequest){

        List<UUID> roleIds = inviteEmployeeRequest.getRoleIds();
        List<Role> roles = roleService.fetchRoleByIds(roleIds);
        Employee manager = employeeRepository.findById(inviteEmployeeRequest.getManagerSysId()).orElse(null);
        Employee invitee = Employee.builder()
                .employeeCode(SystemUtility.generate("EMPLOYEE"))
                .active(true)
                .roles(roles)
                .fullName(inviterEmp.getFullName())
                .workLocation(inviteEmployeeRequest.getWorkLocation())
                .company(inviterEmp.getCompany())
                .userType(UserType.EMPLOYEE)
                .manager(manager)
                .department(inviteEmployeeRequest.getDepartment())
                .email(inviteEmployeeRequest.getEmail())
                .designation(inviteEmployeeRequest.getDesignation())
                .mobileNumber(inviteEmployeeRequest.getMobileNumber())
                .passwordHash(SystemUtility.generateRandomPassword(10))
                .createdBy(inviterEmp.getEmail())
                .updatedBy(inviterEmp.getEmail())
                .build();
        employeeRepository.save(invitee);

        // Invitation Mail

        // We will call notification service
        Notification notification = Notification.builder()
                .notificationId(SystemUtility.generate("NOTIFICATION"))
                .notificationChannel(NotificationChannel.MAIL)
                .receipts(List.of(invitee, inviterEmp, manager))
                .notificationPriority(NotificationPriority.URGENT)
                .notificationStatus(NotificationStatus.DRAFT)
                .templateId(NotificationTemplateConfig.COMPANY_REGISTRATION_ADMIN_CREDENTIALS_ID)
                .createdBy("system")
                .updatedBy("system")
                .build();
        // What context ? ->
        // NotificationContext ->
        NotificationContext notificationContext = new NotificationContext();
        Map<String, String> emailContext = notificationContext.getEmailContext();

        emailContext.put("adminName", invitee.getFullName());
        emailContext.put("companyName", invitee.getCompany().getCompanyName());
        emailContext.put("companyCode", invitee.getCompany().getCompanyCode());
        emailContext.put("adminEmail", invitee.getEmail());
        emailContext.put("temporaryPassword", invitee.getPasswordHash());
        emailContext.put("loginUrl", "https://youtube.com/signin");
        emailContext.put("supportEmail", invitee.getEmail());


        executorService.submit(() -> {
            notificationService.sendNotification(notification, notificationContext);
        });

        return invitee;
    }

    // TODO - Update this method to fetch user by field isEmailSubscribed - For Temp basis we are fetching all the users.
    public List<User> getEmailSubscribedUser(){
        return userRepository.findAll();
    }



    public void saveOrUpdate(User user) {
        this.userRepository.save(user);
    }
}
