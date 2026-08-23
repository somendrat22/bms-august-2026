package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.LoggerConstant;
import com.acciojobs.bms_august.dtos.request.RegisterCompanyDto;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Company;
import com.acciojobs.bms_august.repositories.CompanyRepository;
import com.acciojobs.bms_august.transformers.CompanyTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private UserService userService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          UserService userService){
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    public Company registerCompany(
            RegisterCompanyDto registerCompanyDto,
            CompanyType companyType
    ){
        // 1. We should map registerCompanyDto to CompanyModel - We should write one mapping logic - transformer.
        Company company = CompanyTransformer.mapRegisterCompanyDtoToCompany(registerCompanyDto, companyType);
        // We can get any company object here either it can be event company or either it can be theater company
        // What is our next steps ? Next steps is for TheaterCompany admin user should get created and for event company also admin user will created
        // If the registration if for Theater Company - So, Theater company admin user we need to create
        // If we want to create admin user for theater company - Then we need to create admin role for that theatre company
        // admin role of theatre company can perform different set of operations then admin role of a event company and admin role of a internal company

        // PVR Company -- User (PVR System Adminstrator) - Role(PVR_MAINT) ---- Operations(CREATE_THEATRE, DELETE_THEATRE, INVITE_ROLE)
        // Sunburn ---- User (Sunburn System Adminstrator) - Role(Suburn_MAINT) -- Operations(CREATE_EVENT, DELETE_EVENT)
        // internal --- User(internal System Adminstrator)

        log.info("Calling repo layer to save company record in db.");
        // saveCompanyRecord to the database -
        company = this.saveOrUpdateCompany(company);

        // Calling adminAccount creation flow on the basis of companyType
        log.info("Calling adminAccount creation flow on the basis of companyType : " + companyType.toString());
        userService.createCompanyAdminUser(company);

        // We should notify user regarding the creation of the admin account on the platform for the company
        // Notify -> Mail
        // Notify -> Whatsapp message
        // Notify -> Text SMS 
        return company;
    }

    public Company saveOrUpdateCompany(Company company){
        log.info(String.format(LoggerConstant.BEFORE_DB_SAVE_MESSAGE, "Company", company.toString()));
        company = this.companyRepository.save(company);
        log.info(String.format(LoggerConstant.AFTER_DB_SAVE_MESSAGE, "Company", company.toString()));
        return company;

    }


}
