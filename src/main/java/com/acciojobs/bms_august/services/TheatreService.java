package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.NotificationTemplateConfig;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.dtos.request.CreateTheatreRequest;
import com.acciojobs.bms_august.dtos.request.RegisterCompanyDto;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.models.Company;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.models.Theater;
import com.acciojobs.bms_august.repositories.TheatreRepository;
import com.acciojobs.bms_august.utilities.SystemUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Resposblity is to handle theatres related logic
 */
@Service
public class TheatreService {

    private CompanyService companyService;
    private TheatreRepository theatreRepository;
    private ExecutorService executorService;
    private NotificationService notificationService;

    @Autowired
    public TheatreService(CompanyService companyService,
                          TheatreRepository theatreRepository,
                          NotificationService notificationService,
                          ExecutorService executorService){
        this.companyService = companyService;
        this.theatreRepository = theatreRepository;
        this.notificationService = notificationService;
        this.executorService = executorService;
    }


    /**
     * registerTheatreCompany - Will Internally call CompanyService for the registration.
     * @param registerCompanyDto
     */
    public Company registerTheatreCompany(RegisterCompanyDto registerCompanyDto){
        // companyService -> registerCompany(dto, TheatreCompany)
        return companyService.registerCompany(registerCompanyDto, CompanyType.THEATER_COMPANY);
    }

    public Theater registerTheatre(
            Employee creator,
            CreateTheatreRequest createTheatreRequest
    ){
        Theater theater = toEntity(createTheatreRequest, creator.getCompany(), creator);
        this.theatreRepository.save(theater);
        // Notify

        Notification notification = Notification.builder()
                .notificationId(SystemUtility.generate("NOTIFICATION"))
                .notificationChannel(NotificationChannel.MAIL)
                .receipts(List.of(creator, creator.getManager()))
                .notificationPriority(NotificationPriority.URGENT)
                .notificationStatus(NotificationStatus.DRAFT)
                .templateId(NotificationTemplateConfig.THEATRE_REGISTRATION_TEMPLATE_ID)
                .createdBy("system")
                .updatedBy("system")
                .build();

        NotificationContext notificationContext = new NotificationContext();
        Map<String, String> context = notificationContext.getEmailContext();

        context.put("theaterName", theater.getTheaterName());
        context.put("theaterCode", theater.getTheaterCode());
        context.put("city", theater.getCity());
        context.put("state", theater.getState());

        executorService.submit(() -> {
            notificationService.sendNotification(notification, notificationContext);
        });

        return theater;
    }




    public Theater toEntity(CreateTheatreRequest request, Company company, Employee creator) {
        return Theater.builder()
                .theaterCode(SystemUtility.generate("THEATRE"))
                .theaterName(request.getTheaterName())
                .company(company)
                .description(request.getDescription())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .parkingAvailable(request.isParkingAvailable())
                .foodCourtAvailable(request.isFoodCourtAvailable())
                .wheelchairAccessible(request.isWheelchairAccessible())
                .dolbyAtmosSupported(request.isDolbyAtmosSupported())
                .imaxSupported(request.isImaxSupported())
                .createdBy(creator.getEmail())
                .updatedBy(creator.getEmail())
                .build();
    }
}
