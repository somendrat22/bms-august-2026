package com.acciojobs.bms_august.enums;

public enum OperationType {

    CREATE_ROLE,
    INVITE_EMPLOYEE,
    CREATE_THEATRE,
    CREATE_HALL,
    CREATE_MOVIE("CREATE_COMPANY");

    private final String value;

    OperationType() {
        this.value = name();
    }

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
