package com.acciojobs.bms_august.dtos.common;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class NotificationContext {
    private HashMap<String, String> emailContext = new HashMap<>();
    private HashMap<String, String> whatsappContext = new HashMap<>();
    private HashMap<String, String> smsContext = new HashMap<>();
    private HashMap<String, String> inAppContext = new HashMap<>();
}
