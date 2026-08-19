package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Operation;
import com.acciojobs.bms_august.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    private OperationService operationService;

    @Autowired
    public RoleService(OperationService operationService){
        this.operationService = operationService;
    }

    public Role createAdminRoleByCompanyType(String companyName,
                                             CompanyType companyType){
        List<Operation> operations = operationService.getOperationsForAdminByCompanyType(companyType);
        return Role.builder()
                .roleName(companyName + "_" + SystemConstant.DEFAULT_ADMIN_NAME)
                .operations(operations)
                .createdBy("system")
                .updatedBy("system")
                .build();
    }


}
