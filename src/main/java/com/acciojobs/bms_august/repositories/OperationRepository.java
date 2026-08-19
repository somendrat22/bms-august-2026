package com.acciojobs.bms_august.repositories;

import com.acciojobs.bms_august.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {

    @Query(value = "select * from operations where operation_category =:operationCategory or operation_category = 'COMMON_OPERATION'", nativeQuery = true)
    public List<Operation> fetchAllOperationByCategory(String operationCategory); // THEATRE_COMPANY, EVENT_COMPANY, INTERNAL_COMPANY, CUSTOMER
}
