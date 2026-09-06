package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.LoggerConstant;
import com.acciojobs.bms_august.constants.NotificationTemplateConfig;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.dtos.request.RegisterCompanyDto;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.models.Company;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.repositories.CompanyRepository;
import com.acciojobs.bms_august.transformers.CompanyTransformer;
import com.acciojobs.bms_august.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;


@Slf4j
@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private UserService userService;
    private NotificationService notificationService;
    private ExecutorService executorService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          UserService userService,
                          NotificationService notificationService,
                          ExecutorService executorService){
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.executorService = executorService;
    }

    private void generateSystemAdminAndSendNotification(Company company){
        // Calling adminAccount creation flow on the basis of companyType
        log.info("Calling adminAccount creation flow on the basis of companyType : " + company.getCompanyType().toString());
        Employee admin = userService.createCompanyAdminUser(company);

        // We will call notification service
        Notification notification = Notification.builder()
                .notificationId(SystemUtility.generate("NOTIFICATION"))
                .notificationChannel(NotificationChannel.MAIL)
                .receipts(List.of(admin))
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

        emailContext.put("adminName", admin.getFullName());
        emailContext.put("companyName", company.getCompanyName());
        emailContext.put("companyCode", company.getCompanyCode());
        emailContext.put("adminEmail", admin.getEmail());
        emailContext.put("temporaryPassword", admin.getPasswordHash());
        emailContext.put("loginUrl", "https://youtube.com/signin");
        emailContext.put("supportEmail", admin.getEmail());


        executorService.submit(() -> {
            notificationService.sendNotification(notification, notificationContext);
        });
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

       this.generateSystemAdminAndSendNotification(company);

        return company;
    }

    private Company saveOrUpdateCompany(Company company){
        log.info(String.format(LoggerConstant.BEFORE_DB_SAVE_MESSAGE, "Company", company.toString()));
        company = this.companyRepository.save(company);
        log.info(String.format(LoggerConstant.AFTER_DB_SAVE_MESSAGE, "Company", company.toString()));
        return company;
    }


    private Company generateInternalCompanyObject(){
        return Company.builder()
                .companyCode(SystemUtility.generate("COMPANY"))
                .companyName("Book My Show")
                .companyType(CompanyType.INTERNAL)
                .legalName("August Batch Spring Module 2 Pvt. Ltd.")
                .registrationNumber("U72900KA2021PTC156842")
                .gstNumber("29AABCV7824M1ZT")
                .panNumber("AABCV7824M")
                .email("internal@bookmyshow.com")
                .phoneNumber("+91-12-49672318")
                .website("https://www.bookmyshow.com")
                .addressLine1("VertexNova Tech Park, Whitefield")
                .addressLine2("ITPL Main Road")
                .city("Bengaluru")
                .state("Karnataka")
                .country("India")
                .postalCode("560066")
                .logoUrl("https://www.bookmyshow.com/assets/logo.png")
                .active(true)
                .createdBy("system")
                .updatedBy("system")
                .build();
    }

    private boolean isInternalCompanyAlreadyPresent(){
        List<Company> companies = this.fetchInternalCompany();
        return !companies.isEmpty();
    }

    private List<Company> fetchInternalCompany(){
        return companyRepository.findByCompanyType(CompanyType.INTERNAL);
    }

    public void registerInternalCompany(){
        log.info("Register Internal Company process triggered");
        if(isInternalCompanyAlreadyPresent()){
            log.info("Internal company already registered");
            return;
        }
        Company company = this.generateInternalCompanyObject();
        log.info("Company object got created : {}", company.toString());
        this.saveOrUpdateCompany(company);
        log.info("Company object got saved in DB : {} ", company.toString());
        this.generateSystemAdminAndSendNotification(company);
    }


}
