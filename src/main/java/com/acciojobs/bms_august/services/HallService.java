package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.NotificationTemplateConfig;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.dtos.request.CreateHallRequest;
import com.acciojobs.bms_august.dtos.request.CreateHallSeatMappingRequest;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.exceptions.BMSUnauthorizedException;
import com.acciojobs.bms_august.models.*;
import com.acciojobs.bms_august.repositories.HallRepository;
import com.acciojobs.bms_august.repositories.SeatMappingRepository;
import com.acciojobs.bms_august.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class HallService{

    private TheatreService theatreService;
    private HallRepository hallRepository;
    private SeatMappingRepository seatMappingRepository;
    private ExecutorService executorService;
    private NotificationService notificationService;

    @Autowired
    public HallService(TheatreService theatreService,
                       HallRepository hallRepository,
                       SeatMappingRepository seatMappingRepository,
                       ExecutorService executorService,
                       NotificationService notificationService){
        this.theatreService = theatreService;
        this.hallRepository = hallRepository;
        this.seatMappingRepository = seatMappingRepository;
        this.executorService = executorService;
        this.notificationService = notificationService;
    }

    private Hall saveOrUpdateHall(Hall hall){
        return this.hallRepository.save(hall);
    }

    private SeatMapping saveOrUpdateSeatMapping(SeatMapping seatMapping){
        return this.seatMappingRepository.save(seatMapping);
    }

    public SeatMapping createSeatMapping(CreateHallSeatMappingRequest createHallSeatMappingRequest, Hall hall, Employee creator){
        SeatMapping seatMapping = SeatMapping.builder()
                .seatBreak(createHallSeatMappingRequest.getSeatBreak())
                .seatRange(createHallSeatMappingRequest.getSeatRange())
                .hall(hall)
                .row(createHallSeatMappingRequest.getRow())
                .createdBy(creator.getEmail())
                .updatedBy(creator.getEmail())
                .build();

        return this.saveOrUpdateSeatMapping(seatMapping);
    }

    public List<SeatMapping> createSeatMappings(List<CreateHallSeatMappingRequest> createHallSeatMappingRequests,
                                   Hall hall,
                                   Employee creator){
        List<SeatMapping> seatMappings = new ArrayList<>();
        for(CreateHallSeatMappingRequest createHallSeatMappingRequest : createHallSeatMappingRequests){
            seatMappings.add(this.createSeatMapping(createHallSeatMappingRequest, hall,creator));
        }
        return seatMappings;
    }

    public Hall registerHall(CreateHallRequest createHallRequest, Employee creator){
        Theater theater = theatreService.fetchTheatreBySysId(createHallRequest.getTheaterSysId());
        if(!creator.getCompany().equals(theater.getCompany())){
            throw new BMSUnauthorizedException(String.format("User is not allowed to create hall for this theatre %s,", theater.getSysId().toString()));
        }
        Hall hall = this.mapToHall(createHallRequest, theater, creator);
        // Save the hall object in the db
        hall = this.saveOrUpdateHall(hall);

        // We need to create and save seat mappings
        this.createSeatMappings(createHallRequest.getSeatMappingRequests(), hall, creator);

        // Notify - Creator and creator manager that we have created hall

        Notification notification = Notification.builder()
                .notificationId(SystemUtility.generate("NOTIFICATION"))
                .notificationChannel(NotificationChannel.MAIL)
                .receipts(List.of(creator, creator.getManager()))
                .notificationPriority(NotificationPriority.URGENT)
                .notificationStatus(NotificationStatus.DRAFT)
                .templateId(NotificationTemplateConfig.HALL_REGISTRATION_TEMPLATE_ID)
                .createdBy("system")
                .updatedBy("system")
                .build();

        NotificationContext notificationContext = new NotificationContext();
        Map<String, String> context = notificationContext.getEmailContext();

        context.put("theaterName", theater.getTheaterName());
        context.put("theaterCode", theater.getTheaterCode());
        context.put("city", theater.getCity());
        context.put("state", theater.getState());

        context.put("hallName", hall.getHallName());
        context.put("hallCode", hall.getHallCode());
        context.put("floorNumber", hall.getFloorNumber().toString());
        context.put("totalSeats", hall.getTotalSeats().toString());
        context.put("hallType", hall.getHallType().toString());

        context.put("wheelchairAccessible", hall.isWheelchairAccessible() ? "TRUE" : "FALSE");
        context.put("reclinerSeatsAvailable", hall.isReclinerSeatsAvailable() ? "TRUE" : "FALSE");
        context.put("dolbyAtmosSupported", hall.isDolbyAtmosSupported() ? "TRUE" : "FALSE");
        context.put("threeDSupported", hall.isThreeDSupported()? "TRUE" : "FALSE");
        context.put("imaxSupported", hall.isImaxSupported()? "TRUE" : "FALSE");

        executorService.submit(() -> {
            notificationService.sendNotification(notification, notificationContext);
        });

        return hall;
    }

    public Hall mapToHall(CreateHallRequest request, Theater theater, Employee creator) {

        return Hall.builder()
                .hallCode(SystemUtility.generate("HALL"))
                .hallName(request.getHallName())
                .theater(theater)
                .floorNumber(request.getFloorNumber())
                .totalSeats(request.getTotalSeats())
                .hallType(request.getHallType())

                // Facilities
                .wheelchairAccessible(request.isWheelchairAccessible())
                .reclinerSeatsAvailable(request.isReclinerSeatsAvailable())
                .dolbyAtmosSupported(request.isDolbyAtmosSupported())
                .threeDSupported(request.isThreeDSupported())
                .imaxSupported(request.isImaxSupported())

                // Maintenance / Operations
                .active(true)
                .underMaintenance(request.isUnderMaintenance())
                .maintenanceRemarks(request.getMaintenanceRemarks())
                .createdBy(creator.getEmail())
                .updatedBy(creator.getUpdatedBy())

                .build();
    }


}
