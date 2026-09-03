package com.acciojobs.bms_august.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {
    private String roleName;
    private List<String> operationName;
}
