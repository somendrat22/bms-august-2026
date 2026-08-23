package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.enums.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * This service will be capable of sending notification by any mode or by any channel
 * Channel -> MAIL, WHATSAPP MESSAGE, TEXT SMS
 */
@Service
public class NotificationService {

    public String[] getAllSupportedNotificationChannel(){
        return Arrays.stream(NotificationChannel.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }


    /**
     * Whenever and wherever you want to send any kind of notification you will call this method.
     */
    public void sendNotification(

    ){
        String [] supportedChannels = this.getAllSupportedNotificationChannel();
        for(String channel : supportedChannels){
            switch (channel){
                case NotificationChannel.MAIL.name():
                    // -> EmailService
                case

            }
        }
    }

}
