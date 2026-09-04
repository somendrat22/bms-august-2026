package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.LoggerConstant;
import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.dtos.request.CreateRoleRequest;
import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Operation;
import com.acciojobs.bms_august.models.Role;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RoleService {

    private OperationService operationService;
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(OperationService operationService,
                       RoleRepository roleRepository){
        this.operationService = operationService;
        this.roleRepository = roleRepository;
    }

    public Role createAdminRoleByCompanyType(String companyName,
                                             CompanyType companyType){
        List<Operation> operations = operationService.getOperationsForAdminByCompanyType(companyType);
        Role adminRole = Role.builder()
                .roleName(companyName + "_" + SystemConstant.DEFAULT_ADMIN_NAME)
                .operations(operations)
                .createdBy("system")
                .updatedBy("system")
                .build();
        return this.saveOrUpdateRole(adminRole);
    }

    public Role  saveOrUpdateRole(Role role){
        log.info(String.format(LoggerConstant.BEFORE_DB_SAVE_MESSAGE, "Role", role.toString()));
        role = this.roleRepository.save(role);
        log.info(String.format(LoggerConstant.AFTER_DB_SAVE_MESSAGE, "Role", role.toString()));
        return role;
    }

    public List<Role> fetchRoleByIds(List<UUID> roleSysId){
        List<Role> roles = new ArrayList<>();
        for(UUID sysId : roleSysId){
            Role role = roleRepository.findById(sysId).orElse(null);
            if(role == null){
                log.warn(String.format("Role with sysId %s does not exist", sysId.toString()));
                continue;
            }
            roles.add(role);
        }
        return roles;
    }

    public Role createRole(Employee employee, CreateRoleRequest createRoleRequest){
        // Mapping between DTO and Role Model
        List<String> operationNames = createRoleRequest.getOperationName();
        // From this list we need to get all operations object
        List<Operation> operations = operationService.getOperationsByName(operationNames);
        // roleName
        String prefix = employee.getCompany().getCompanyName();
        String roleName = prefix + "_" + createRoleRequest.getRoleName(); // RECEPTION_SPECIALIST CompanyName

        Role role = Role.builder()
                .roleName(roleName)
                .operations(operations)
                .createdBy(employee.getEmail())
                .updatedBy(employee.getEmail())
                .build();

        // NOTIFY -> User
        return this.saveOrUpdateRole(role);
    }


}
