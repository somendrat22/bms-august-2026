package com.acciojobs.bms_august.constants;

import java.util.UUID;

public final class NotificationTemplateConfig {

    private NotificationTemplateConfig() {
        // Utility class
    }

    // ============================================================
    // SHOW
    // ============================================================

    public static final UUID SHOW_REMINDER =
            UUID.fromString("153a6cee-3a27-4059-999c-a6961db71383");


    // ============================================================
    // TICKET
    // ============================================================



    public static final UUID TICKET_GENERATED =
            UUID.fromString("21e75a9a-3ea6-43fa-b910-a8fbd9f32715");



    public static final UUID CUSTOMER_REGISTRATION_SUCCESS =
            UUID.fromString("392d9d3c-86f9-41f3-a7bc-681450bea90b");


    // ============================================================
    // ADMIN
    // ============================================================

    public static final UUID ADMIN_ACCOUNT_ACTIVATED =
            UUID.fromString("439b0ad4-a742-45a4-aab0-35999b8abe08");


    // ============================================================
    // COMPANY
    // ============================================================

    public static final String COMPANY_REGISTRATION_SUCCESS =
            "COMPANY_REGISTRATION_SUCCESS";

    public static final UUID COMPANY_REGISTRATION_SUCCESS_ID =
            UUID.fromString("445b51ce-d842-4ed0-bba5-a0a5ac1f3071");


    public static final String COMPANY_REGISTRATION_ADMIN_CREDENTIALS =
            "COMPANY_REGISTRATION_ADMIN_CREDENTIALS";

    public static final UUID COMPANY_REGISTRATION_ADMIN_CREDENTIALS_ID =
            UUID.fromString("e3a68491-9a4e-4abe-b01b-9a675c1762ba");


    // ============================================================
    // PAYMENT
    // ============================================================

    public static final String PAYMENT_FAILED =
            "PAYMENT_FAILED";

    public static final UUID PAYMENT_FAILED_ID =
            UUID.fromString("98191306-a749-4a31-8795-4caa1cb59644");


    public static final String PAYMENT_SUCCESS =
            "PAYMENT_SUCCESS";

    public static final UUID PAYMENT_SUCCESS_ID =
            UUID.fromString("b2c5f66f-a2f0-4372-993d-7f22297647d9");


    // ============================================================
    // BOOKING
    // ============================================================

    public static final String BOOKING_CANCELLED =
            "BOOKING_CANCELLED";

    public static final UUID BOOKING_CANCELLED_ID =
            UUID.fromString("ac8b9a20-72b3-48d1-8e92-3fdb278884c5");


    public static final String BOOKING_CONFIRMATION =
            "BOOKING_CONFIRMATION";

    public static final UUID BOOKING_CONFIRMATION_ID =
            UUID.fromString("cd275d7f-d3eb-49d7-bab0-c18e5821caaa");


    // ============================================================
    // AUTHENTICATION
    // ============================================================

    public static final String OTP_VERIFICATION =
            "OTP_VERIFICATION";

    public static final UUID OTP_VERIFICATION_ID =
            UUID.fromString("babce163-1154-4a15-b48f-258fafb362fc");


    public static final String PASSWORD_RESET =
            "PASSWORD_RESET";

    public static final UUID PASSWORD_RESET_ID =
            UUID.fromString("c6bf02c9-e9bf-4c46-9a7f-febfe4a6f2a0");


    // ============================================================
    // REFUND
    // ============================================================

    public static final String REFUND_COMPLETED =
            "REFUND_COMPLETED";

    public static final UUID REFUND_COMPLETED_ID =
            UUID.fromString("fb1731c6-56f9-4777-8916-47a3ebd3a23b");
}
