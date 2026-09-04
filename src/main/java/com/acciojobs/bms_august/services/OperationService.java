package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Operation;
import com.acciojobs.bms_august.repositories.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OperationService {

    private OperationRepository operationRepository;

    @Autowired
    public OperationService(OperationRepository operationRepository){
        this.operationRepository = operationRepository;
    }

    public List<Operation> getOperationsForAdminByCompanyType(CompanyType companyType){
        switch (companyType){
            case INTERNAL:
                return operationRepository.fetchAllOperationByCategory("INTERNAL_COMPANY");
            case THEATER_COMPANY:
                return operationRepository.fetchAllOperationByCategory("THEATRE_COMPANY");
            case EVENT_COMPANY:
                return operationRepository.fetchAllOperationByCategory("EVENT_COMPANY");
            default:
                throw new IllegalArgumentException("Illegal value received i.e. " + companyType.toString());
        }
    }

    public List<Operation> getOperationsByName(List<String> operationNames){
        List<Operation> operations = new ArrayList<>();
        for(String oprName : operationNames){
            Operation operation = this.getOperationByName(oprName);
            if (operation == null){
                log.info("Operation with name {} does not exist", oprName);
                continue;
            }
            operations.add(operation);
        }
        return operations;
    }

    public Operation getOperationByName(String operationName){
        // To get the operationByName - operationRepo
        return operationRepository.findByOperationName(operationName).orElse(null);
    }

}
