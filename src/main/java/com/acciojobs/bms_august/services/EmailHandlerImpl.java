package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.models.NotificationTemplate;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.utilities.SystemUtility;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailHandlerImpl implements NotificationHandler{

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailHandlerImpl(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Override
    public NotificationChannel getChannel() {
        return null;
    }

    /**
     * This method is very generic to send any kind of notification
     * @param notification
     * @param context
     */
    public Notification sendNotification(Notification notification,
                                         HashMap<String, String> context,
                                         NotificationTemplate notificationTemplate) {

        log.info(
                "Processing notification. NotificationId: {}, Priority: {}, Status: {}, Template: {}",
                notification.getSysId(),
                notification.getNotificationPriority(),
                notification.getNotificationStatus(),
                notificationTemplate.getTemplateName()
        );

        try {

            if (notification.getNotificationPriority().equals(NotificationPriority.URGENT)) {

                log.info(
                        "Processing URGENT notification immediately. NotificationId: {}",
                        notification.getSysId()
                );

                this.sendEmail(notification, notificationTemplate, context);

                notification.setNotificationStatus(NotificationStatus.DELIVERED);

                log.info(
                        "URGENT notification delivered successfully. NotificationId: {}, Status: {}",
                        notification.getSysId(),
                        notification.getNotificationStatus()
                );

            } else if (
                    notification.getNotificationPriority().equals(NotificationPriority.NORMAL)
                            && notification.getNotificationStatus() == NotificationStatus.DRAFT
            ) {

                log.info(
                        "Queueing NORMAL notification. NotificationId: {}, CurrentStatus: {}",
                        notification.getSysId(),
                        notification.getNotificationStatus()
                );

                notification.setNotificationStatus(NotificationStatus.IN_QUEUE);

                log.info(
                        "Notification queued successfully. NotificationId: {}, NewStatus: {}",
                        notification.getSysId(),
                        notification.getNotificationStatus()
                );

            } else if (notification.getNotificationStatus() == NotificationStatus.IN_QUEUE) {

                // IN_QUEUE

                log.info(
                        "Processing queued notification. NotificationId: {}, Priority: {}",
                        notification.getSysId(),
                        notification.getNotificationPriority()
                );

                this.sendEmail(notification, notificationTemplate, context);

                notification.setNotificationStatus(NotificationStatus.DELIVERED);

                log.info(
                        "Queued notification delivered successfully. NotificationId: {}, Status: {}",
                        notification.getSysId(),
                        notification.getNotificationStatus()
                );

            } else {

                log.warn(
                        "Invalid notification state. NotificationId: {}, Priority: {}, Status: {}",
                        notification.getSysId(),
                        notification.getNotificationPriority(),
                        notification.getNotificationStatus()
                );

                throw new IllegalArgumentException(
                        "Illegal notification priority/status combination"
                );
            }

            return notification;

        } catch (Exception e) {

            log.error(
                    "Failed to process notification. NotificationId: {}, Priority: {}, Status: {}, Template: {}",
                    notification.getSysId(),
                    notification.getNotificationPriority(),
                    notification.getNotificationStatus(),
                    notificationTemplate.getTemplateName(),
                    e
            );

            throw e;
        }
    }

    public void sendEmail(Notification notification,
                          NotificationTemplate notificationTemplate,
                          HashMap<String, String> context) {

        log.info("Starting email notification. Template: {}, Recipients: {}",
                notificationTemplate.getTemplateName(),
                notification.getReceipts().size());

        try {

            String htmlMailBody = SystemUtility.populateValueInTemplate(
                    context,
                    notificationTemplate.getEmailTemplate()
            );

            String subject = SystemUtility.populateValueInTemplate(
                    context,
                    notificationTemplate.getSubject()
            );

            log.debug("Email template populated successfully. Template: {}, Subject: {}",
                    notificationTemplate.getTemplateName(),
                    subject);

            List<String> emailIds = new ArrayList<>();

            for (User user : notification.getReceipts()) {
                if (user.getEmail() != null && !user.getEmail().isBlank()) {
                    emailIds.add(user.getEmail());
                }
            }

            if (emailIds.isEmpty()) {
                log.warn("No valid email recipients found. Template: {}",
                        notificationTemplate.getTemplateName());
                return;
            }

            log.info("Preparing email. Template: {}, RecipientCount: {}",
                    notificationTemplate.getTemplateName(),
                    emailIds.size());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setText(htmlMailBody, true);
            mimeMessageHelper.setTo(emailIds.toArray(new String[0]));
            mimeMessageHelper.setSubject(subject);

            log.debug("Email message prepared successfully. Template: {}, RecipientCount: {}",
                    notificationTemplate.getTemplateName(),
                    emailIds.size());

            javaMailSender.send(mimeMessage);

            log.info("Email notification sent successfully. Template: {}, RecipientCount: {}",
                    notificationTemplate.getTemplateName(),
                    emailIds.size());

        } catch (Exception e) {

            log.error("Failed to send email notification. Template: {}, RecipientCount: {}",
                    notificationTemplate.getTemplateName(),
                    notification.getReceipts() != null
                            ? notification.getReceipts().size()
                            : 0,
                    e);

            throw new RuntimeException("Failed to send email notification", e);
        }
    }

}
