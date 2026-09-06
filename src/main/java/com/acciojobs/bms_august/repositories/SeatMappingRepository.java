package com.acciojobs.bms_august.repositories;

import com.acciojobs.bms_august.models.SeatMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatMappingRepository extends JpaRepository<SeatMapping, UUID> {
}
