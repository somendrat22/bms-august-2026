package com.acciojobs.bms_august.models;

import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification extends GlobalRecord{
    private String notificationId;
    @Enumerated(EnumType.STRING)
    private NotificationChannel notificationChannel;
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    @Enumerated(EnumType.STRING)
    private NotificationPriority notificationPriority;
    @ManyToMany
    private List<User> receipts;
    private UUID templateId;
}
