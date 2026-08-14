package com.acciojobs.bms_august.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class Role extends GlobalRecord{
    @Column(unique = true, nullable = false)
    private String roleName; // COMPANY_NAME + roleName
    @ManyToMany
    private List<Operation> operations;
}
