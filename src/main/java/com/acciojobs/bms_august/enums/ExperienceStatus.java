package com.acciojobs.bms_august.enums;

public enum ExperienceStatus {
    DRAFT,          // Created but not yet published

    SCHEDULED,      // Published and available for booking

    BOOKING_OPEN,   // Ticket sales are open

    BOOKING_CLOSED, // Ticket sales closed before start

    SOLD_OUT,       // No tickets available

    CANCELLED,      // Experience cancelled

    IN_PROGRESS,    // Currently happening

    COMPLETED,      // Successfully finished

    POSTPONED,      // Rescheduled to a future date

    ARCHIVED        // Historical record, hidden from active listings
}
