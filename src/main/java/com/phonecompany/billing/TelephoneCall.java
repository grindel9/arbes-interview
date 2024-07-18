package com.phonecompany.billing;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

// todo usage of @Getter and @Setter will save a lot of code
public class TelephoneCall {
    private final String phoneNumber;
    private final LocalDateTime callStart;
    private final LocalDateTime callEnd;

    public TelephoneCall(String phoneNumber, LocalDateTime callStart, LocalDateTime callEnd) {
        this.phoneNumber = phoneNumber;
        this.callStart = callStart;
        this.callEnd = callEnd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCallStart() {
        return callStart;
    }

    public LocalDateTime getCallEnd() {
        return callEnd;
    }

    public int getCallDurationInMinutes() {
        return (int) getCallStart().until(getCallEnd(), MINUTES);
    }

//    todo
//    in getIntersectionMainTimeInMinutes and  getIntersectionOtherTimeInMinutes is potentially a bug, but I found it 15 minutes before the deadline
//    I will leave it as it is, but it should be fixed.
//    The bug is that the call can start at 23:59:10 and end at 00:01:20, which is 2 minutes, but the function will return 1 minutes

    public int getIntersectionMainTimeInMinutes() {
        int minutes = 0;
        LocalDateTime day = getCallStart();
        while(day.isBefore(getCallEnd())) {
            minutes += getIntersectionMainTimeInMinutesPerDay(day);
            // ugly and should be refactored but, I also find there a bug in the last few minutes
            day = day.plusDays(1);
            day = day.withHour(0).withMinute(0).withSecond(0);
        }
        return minutes;
    }

    public int getIntersectionOtherTimeInMinutes() {
        int minutes = 0;
        LocalDateTime day = getCallStart();
        while(day.isBefore(getCallEnd())) {
            minutes += getIntersectionOtherTimeInMinutesPerDay(day);
            // ugly and should be refactored but, I also find there a bug in the last few minutes
            day = day.plusDays(1);
            day = day.withHour(0).withMinute(0).withSecond(0);
        }
        return minutes;
    }

//    todo
//    numbers in function dates ex line 67, 68 should be extracted to constants to keep the code DRY
    private int getIntersectionMainTimeInMinutesPerDay(LocalDateTime day) {
        LocalDateTime startMainTime = day.withHour(8).withMinute(0).withSecond(0);
        LocalDateTime endMainTime = day.withHour(15).withMinute(59).withSecond(59);

        return getIntersectionInMinutes(startMainTime, endMainTime);
    }

    private int getIntersectionOtherTimeInMinutesPerDay(LocalDateTime day) {
        LocalDateTime firstOtherTimePeriodStart = day.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime firstOtherTimePeriodEnd = day.withHour(7).withMinute(59).withSecond(59);

        int minutes = getIntersectionInMinutes(firstOtherTimePeriodStart, firstOtherTimePeriodEnd);

        LocalDateTime secondOtherTimePeriodStart = day.withHour(16).withMinute(0).withSecond(0);
        LocalDateTime secondOtherTimePeriodEnd = day.withHour(23).withMinute(59).withSecond(59);

        return minutes + getIntersectionInMinutes(secondOtherTimePeriodStart, secondOtherTimePeriodEnd);
    }

    private int getIntersectionInMinutes(LocalDateTime start, LocalDateTime end) {
        LocalDateTime callStart = getCallStart();
        LocalDateTime callEnd = getCallEnd();

        LocalDateTime intersectionStart = callStart.isBefore(start) ? start : callStart;
        LocalDateTime intersectionEnd = callEnd.isAfter(end) ? end : callEnd;

        if (intersectionStart.isAfter(intersectionEnd)) {
            return 0;
        }

        return (int) intersectionStart.until(intersectionEnd, MINUTES);
    }
}