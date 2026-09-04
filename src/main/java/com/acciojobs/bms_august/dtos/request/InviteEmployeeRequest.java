package com.acciojobs.bms_august.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteEmployeeRequest {
    private String designation;
    private String department;
    private String workLocation;
    private UUID managerSysId;
    private String fullName;
    private String email;
    private String mobileNumber;
    private List<UUID> roleIds;
}
