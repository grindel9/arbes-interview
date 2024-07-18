package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    private static final double COST_PER_MINUTE_MAIN_TIME = 1;
    private static final double COST_PER_MINUTE_OTHER_TIME = 0.5;
    private static final double COST_PER_MINUTE_DISCOUNT = 0.2;
    private static final int FIVE_MINUTES_DURATION = 5;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public BigDecimal calculate(String phoneLog) {
        BigDecimal totalCost = new BigDecimal(0);

//        empty list check
        if (phoneLog == null || phoneLog.isEmpty()) {
            return totalCost;
        }

        List<TelephoneCall> calls = extractCallsFromString(phoneLog);
        HashMap<String, TelephoneCallPair> phoneCallFrequency = new HashMap<>();

//        calc cost of each call and add to total cost
        double callCost;
        for (TelephoneCall call : calls) {
            calculateCost(call);
            callCost = calculateCost(call);
            totalCost = totalCost.add(BigDecimal.valueOf(callCost));

            if (phoneCallFrequency.containsKey(call.getPhoneNumber())) {
                phoneCallFrequency.get(call.getPhoneNumber()).addCallCost(callCost);
                phoneCallFrequency.get(call.getPhoneNumber()).incrementCallFrequency();
            } else {
                TelephoneCallPair telephoneCallPair = new TelephoneCallPair(call.getPhoneNumber());
                telephoneCallPair.addCallCost(callCost);
                telephoneCallPair.incrementCallFrequency();
                phoneCallFrequency.put(call.getPhoneNumber(), telephoneCallPair);
            }
//            Usefully for debugging
            System.out.println("call duration: " + call.getCallDurationInMinutes() + " cost: " + calculateCost(call));
        }

//        find and subtract most called phone number, if there are multiple, subtract the one with the highest arithmetic phone number value
        TelephoneCallPair maxFrequency = phoneCallFrequency.values().stream()
                .max(Comparator.comparing(TelephoneCallPair::getCallFrequency)
                        .thenComparing(telephoneCallPair -> new BigDecimal(telephoneCallPair.getPhoneNumber())))
                .orElseThrow();

        return totalCost.subtract(BigDecimal.valueOf(maxFrequency.getTotalCallCost()));
    }

    private List<TelephoneCall> extractCallsFromString(String phoneLog) {
        String[] lines = phoneLog.split("\n");
        return Arrays.stream(lines)
                .map(line -> line.split(","))
                .map(parts -> new TelephoneCall(parts[0], LocalDateTime.parse(parts[1], formatter), LocalDateTime.parse(parts[2], formatter)))
                .toList();
    }

    private double calculateCost(TelephoneCall call) {
        if (call.getCallDurationInMinutes() < FIVE_MINUTES_DURATION){
            int mainMinutes = call.getIntersectionMainTimeInMinutes();
            int otherMinutes = call.getIntersectionOtherTimeInMinutes();
            return mainMinutes * COST_PER_MINUTE_MAIN_TIME + otherMinutes * COST_PER_MINUTE_OTHER_TIME;
        }
        // split phone call longer than 5 minutes into 2 calls and resolve them separately
        LocalDateTime firstFiveMinutes = call.getCallStart().plusMinutes(5);

        TelephoneCall firstFiveMinutesCall = new TelephoneCall(call.getPhoneNumber(), call.getCallStart(), firstFiveMinutes);
        TelephoneCall discountedCall = new TelephoneCall(call.getPhoneNumber(), firstFiveMinutes, call.getCallEnd());

        int mainMinutes = firstFiveMinutesCall.getIntersectionMainTimeInMinutes();
        int otherMinutes = firstFiveMinutesCall.getIntersectionOtherTimeInMinutes();
        double firstFiveMinutesCallCost = mainMinutes * COST_PER_MINUTE_MAIN_TIME + otherMinutes * COST_PER_MINUTE_OTHER_TIME;

        return firstFiveMinutesCallCost + discountedCall.getCallDurationInMinutes() * COST_PER_MINUTE_DISCOUNT;
    }

}
