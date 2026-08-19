package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.enums.CompanyType;
import com.acciojobs.bms_august.models.Operation;
import com.acciojobs.bms_august.repositories.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
