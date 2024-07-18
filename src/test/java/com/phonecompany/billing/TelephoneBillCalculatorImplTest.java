package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneBillCalculatorImplTest {

    @Test
    void testNullInput() {
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        assertEquals(BigDecimal.ZERO, telephoneBillCalculator.calculate(null));
    }

    @Test
    void testEmptyInput() {
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        assertEquals(BigDecimal.ZERO, telephoneBillCalculator.calculate(""));
    }

    @Test
    void testOneCallIsEqualToZero() {
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        String input = "999999999999,13-01-2020 08:10:15,13-01-2020 08:12:57";
        assertEquals(BigDecimal.valueOf(0.0), telephoneBillCalculator.calculate(input));
    }

    @Test
    void testTwoCallsFromSameNumberIsEqualToZero(){
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        String input = "999999999999,13-01-2020 08:10:15,13-01-2020 08:12:57\n999999999999,13-01-2020 08:10:15,13-01-2020 08:12:57";
        assertEquals(BigDecimal.valueOf(0.0), telephoneBillCalculator.calculate(input));
    }

    @Test
    void testLessThanFiveMinutesMainTimeCall(){
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        String input = "420774577453,13-01-2020 15:10:15,13-01-2020 15:12:57\n999999999999,18-01-2020 08:59:20,18-01-2020 09:10:00";
        assertEquals(BigDecimal.valueOf(2.0), telephoneBillCalculator.calculate(input));
    }

    @Test
    void testLessThanFiveMinutesOtherTimeCall(){
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        String input = "420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57\n999999999999,18-01-2020 08:59:20,18-01-2020 09:10:00";
        assertEquals(BigDecimal.valueOf(1.0), telephoneBillCalculator.calculate(input));
    }

    @Test
    void testCallLongerThanFiveMinutesOtherTimeCall(){
        TelephoneBillCalculatorImpl telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        String input = "420774577453,13-01-2020 18:10:00,13-01-2020 18:25:00\n999999999999,18-01-2020 08:59:20,18-01-2020 09:10:00";
        assertEquals(BigDecimal.valueOf(4.5), telephoneBillCalculator.calculate(input));
    }

    // todo there could possibly be more tests
}