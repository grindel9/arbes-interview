package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneCallTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Test
    void testMainTimePeriod() {
        TelephoneCall telephoneCall = new TelephoneCall("420774577453",
                LocalDateTime.parse("13-01-2020 08:59:20", formatter),
                LocalDateTime.parse("13-01-2020 09:10:00", formatter));
        assertEquals(10, telephoneCall.getIntersectionMainTimeInMinutes());
    }

    @Test
    void testOtherTimePeriod() {
        TelephoneCall telephoneCall = new TelephoneCall("420774577453",
                LocalDateTime.parse("13-01-2020 18:10:15", formatter),
                LocalDateTime.parse("13-01-2020 18:12:57", formatter));
        assertEquals(2, telephoneCall.getIntersectionOtherTimeInMinutes());
    }

    @Test
    void testOtherTimePeriodOverMidnight() {
        TelephoneCall telephoneCall = new TelephoneCall("420774577453",
                LocalDateTime.parse("13-01-2020 23:58:50", formatter),
                LocalDateTime.parse("14-01-2020 00:01:10", formatter));
        assertEquals(2, telephoneCall.getIntersectionOtherTimeInMinutes());
    }
}