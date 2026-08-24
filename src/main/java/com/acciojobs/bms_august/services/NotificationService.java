package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.SystemConstant;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.models.NotificationTemplate;
import com.acciojobs.bms_august.repositories.NotificationRepo;
import com.acciojobs.bms_august.repositories.NotificationTemplateRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/**
 * We generally have notification Priority ->
 * 0 -> Send it now
 * 1 -> Send it in bulk
 */

/**
 * This service will be capable of sending notification by any mode or by any channel
 * Channel -> MAIL, WHATSAPP MESSAGE, TEXT SMS
 */
@Slf4j
@Service
public class NotificationService {

    public EmailHandlerImpl emailHandler;
    public NotificationTemplateRepo notificationTemplateRepo;
    public ConcurrentHashMap<UUID, NotificationTemplate> notificationTemplateCache;
    public NotificationRepo notificationRepo;

    @Autowired
    public NotificationService(NotificationTemplateRepo notificationTemplateRepo,
                               NotificationRepo notificationRepo,
                               EmailHandlerImpl emailHandler){
        this.notificationTemplateRepo = notificationTemplateRepo;
        this.notificationRepo = notificationRepo;
        this.emailHandler = emailHandler;
        this.notificationTemplateCache = new ConcurrentHashMap<>();
    }

    // ToDO -> We are not having cache eviction policy. In future implement it
    private NotificationTemplate getNotificationTemplate(UUID templateId){
        // First we will check that do we have this template stored in the cache ?
        // If yes return it from cache if no then make DB call and get the template and store it in cache
        if(notificationTemplateCache.containsKey(templateId)){
            return notificationTemplateCache.get(templateId);
        }else{
            NotificationTemplate notificationTemplate = notificationTemplateRepo.findById(templateId).orElseThrow(
                    () -> new IllegalArgumentException("Illegal Template Id passed")
            );
            notificationTemplateCache.put(templateId, notificationTemplate);
            return notificationTemplate;
        }
    }

    /**
     * Whenever and wherever you want to send any kind of notification you will call this method.
     */
    public Notification sendNotification(
            Notification notification,
            NotificationContext notificationContext
    ) {

        UUID notificationId = notification.getSysId();
        UUID templateId = notification.getTemplateId();
        NotificationChannel notificationChannel = notification.getNotificationChannel();

        log.info(
                "Starting notification processing. NotificationId: {}, TemplateId: {}, Channel: {}, Priority: {}, Status: {}",
                notificationId,
                templateId,
                notificationChannel,
                notification.getNotificationPriority(),
                notification.getNotificationStatus()
        );

        for (int attempt = 1;
             attempt <= SystemConstant.NOTIFICATION_RETRY_ATTEMPT;
             attempt++) {

            try {

                log.info(
                        "Processing notification attempt {}/{}. NotificationId: {}",
                        attempt,
                        SystemConstant.NOTIFICATION_RETRY_ATTEMPT,
                        notificationId
                );

                /*
                 * Inside notification object we are having notification templateId.
                 * We can use that templateId to get the NotificationTemplate object
                 * from the database.
                 */
                log.debug(
                        "Fetching notification template. NotificationId: {}, TemplateId: {}",
                        notificationId,
                        templateId
                );

                NotificationTemplate notificationTemplate =
                        this.getNotificationTemplate(templateId);

                log.debug(
                        "Notification template fetched successfully. NotificationId: {}, TemplateId: {}, TemplateName: {}",
                        notificationId,
                        templateId,
                        notificationTemplate.getTemplateName()
                );

                log.info(
                        "Sending notification. NotificationId: {}, Channel: {}, Attempt: {}",
                        notificationId,
                        notificationChannel,
                        attempt
                );

                switch (notificationChannel) {

                    case MAIL -> {
                        log.info(
                                "Sending email notification. NotificationId: {}",
                                notificationId
                        );

                        emailHandler.sendNotification(
                                notification,
                                notificationContext.getEmailContext(),
                                notificationTemplate
                        );

                        log.info(
                                "Email notification sent successfully. NotificationId: {}",
                                notificationId
                        );
                    }

                    case SMS -> {
                        log.info(
                                "SMS notification channel selected. NotificationId: {}",
                                notificationId
                        );

                        // smsHandler.sendNotification(...);
                    }

                    case WHATSAPP -> {
                        log.info(
                                "WhatsApp notification channel selected. NotificationId: {}",
                                notificationId
                        );

                        // whatsappHandler.sendNotification(...);
                    }

                    case IN_APP -> {
                        log.info(
                                "In-app notification channel selected. NotificationId: {}",
                                notificationId
                        );

                        // inAppHandler.sendNotification(...);
                    }

                    default -> {
                        log.error(
                                "Invalid notification channel. NotificationId: {}, Channel: {}",
                                notificationId,
                                notificationChannel
                        );

                        throw new IllegalArgumentException(
                                "Invalid channel passed: " + notificationChannel
                        );
                    }
                }

                log.debug(
                        "Saving successfully processed notification. NotificationId: {}",
                        notificationId
                );

                Notification savedNotification =
                        this.notificationRepo.save(notification);

                log.info(
                        "Notification processed successfully. NotificationId: {}, FinalStatus: {}",
                        notificationId,
                        savedNotification.getNotificationStatus()
                );

                return savedNotification;

            } catch (Exception e) {

                log.error(
                        "Notification processing failed. NotificationId: {}, Attempt: {}/{}, Channel: {}, Error: {}",
                        notificationId,
                        attempt,
                        SystemConstant.NOTIFICATION_RETRY_ATTEMPT,
                        notificationChannel,
                        e.getMessage(),
                        e
                );

                if (attempt < SystemConstant.NOTIFICATION_RETRY_ATTEMPT) {

                    log.warn(
                            "Retrying notification. NotificationId: {}, NextAttempt: {}",
                            notificationId,
                            attempt + 1
                    );
                } else {

                    log.error(
                            "All notification retry attempts exhausted. NotificationId: {}, TotalAttempts: {}",
                            notificationId,
                            SystemConstant.NOTIFICATION_RETRY_ATTEMPT
                    );
                }
            }
        }

        notification.setNotificationStatus(NotificationStatus.FAILED);

        log.error(
                "Notification marked as FAILED after exhausting all retry attempts. NotificationId: {}, TemplateId: {}, Channel: {}",
                notificationId,
                templateId,
                notificationChannel
        );

        Notification failedNotification =
                this.notificationRepo.save(notification);

        log.info(
                "Failed notification saved to database. NotificationId: {}, Status: {}",
                notificationId,
                failedNotification.getNotificationStatus()
        );

        throw new RuntimeException(String.format("Failed to send notification : %s",notification.toString()));
    }

}
