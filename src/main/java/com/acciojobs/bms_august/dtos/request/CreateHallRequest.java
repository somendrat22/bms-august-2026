package com.acciojobs.bms_august.dtos.request;

import com.acciojobs.bms_august.enums.HallType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHallRequest {
    private String hallName;
    private UUID theaterSysId;
    private Integer floorNumber;
    private Integer totalSeats;
    private HallType hallType;
    private boolean wheelchairAccessible;
    private boolean reclinerSeatsAvailable;
    private boolean dolbyAtmosSupported;
    private boolean threeDSupported;
    private boolean imaxSupported;
    private boolean underMaintenance;
    private String maintenanceRemarks;
    private List<CreateHallSeatMappingRequest> seatMappingRequests;
}
