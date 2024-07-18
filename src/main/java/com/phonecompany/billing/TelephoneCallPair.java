package com.phonecompany.billing;

// todo usage of @Getter and @Setter will save a lot of code
public class TelephoneCallPair {
    private final String phoneNumber;
    private double totalCallCost = 0;
    private int callFrequency = 0;

    public TelephoneCallPair(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getTotalCallCost() {
        return totalCallCost;
    }

    public int getCallFrequency() {
        return callFrequency;
    }

    public void addCallCost(double callCost) {
        totalCallCost += callCost;
    }

    public void incrementCallFrequency() {
        callFrequency++;
    }
}
