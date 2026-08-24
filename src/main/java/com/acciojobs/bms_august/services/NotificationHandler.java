package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.models.NotificationTemplate;

import java.util.HashMap;

public interface NotificationHandler {
    public NotificationChannel getChannel();
    public Notification sendNotification(Notification notification,
                                         HashMap<String, String> context, NotificationTemplate notificationTemplate);
}
