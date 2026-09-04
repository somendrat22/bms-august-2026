package com.acciojobs.bms_august.repositories;

import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    public Optional<Employee> findByEmail(String email);
}
