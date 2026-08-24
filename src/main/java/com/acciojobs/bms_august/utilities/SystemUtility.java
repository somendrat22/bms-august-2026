package com.acciojobs.bms_august.utilities;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SystemUtility {

    private static final SecureRandom random = new SecureRandom();

    public static String generate(String prefix) {
        int randomNumber = 100000 + random.nextInt(900000);
        return prefix + "-" + randomNumber;
    }

    public static String generateRandomPassword(int length) {
        final String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" +
                        "0123456789" +
                        "!@#$%^&*";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(
                    random.nextInt(characters.length())
            ));
        }

        return password.toString();
    }

    public static String populateValueInTemplate(
            HashMap<String, String> context,
            String template
    ){
        for (Map.Entry<String, String> entry : context.entrySet()) {
            String placeholder = "[[${" + entry.getKey() + "}]]";
            template = template.replace(
                    placeholder,
                    entry.getValue()
            );
        }
        return template;
    }
}
