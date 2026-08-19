package com.acciojobs.bms_august.transformers;

import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.enums.UserType;
import com.acciojobs.bms_august.models.Company;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.utilities.SystemUtility;

import java.time.LocalDateTime;

public class UserTransformer {

    public static Employee transformCompanyToAdminUser(Company company){
        return Employee.builder()
                .email(company.getEmail())
                .userType(company.getCompanyType() == CompanyType.INTERNAL ? UserType.INTERNAL : UserType.EMPLOYEE)
                .designation("admin")
                .company(company)
                .employeeCode(SystemUtility.generate("EMP"))
                .email(company.getEmail())
                .fullName(company.getCompanyName() + " " + SystemConstant.DEFAULT_ADMIN_NAME)
                .mobileNumber(company.getPhoneNumber())
                .passwordHash(SystemUtility.generateRandomPassword(SystemConstant.DEFAULT_PASSWORD_LENGTH))
                .lastLoginAt(null)
                .createdBy("system")
                .updatedBy("system")
                .build();
    }

}
