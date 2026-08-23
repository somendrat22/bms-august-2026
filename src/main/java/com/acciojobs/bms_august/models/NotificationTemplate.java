package com.acciojobs.bms_august.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification-templates")
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sysId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    private String subject;
    private String templateName;
    private String emailTemplate;
    private String whatsappTemplate;
    private String smsTemplate;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
