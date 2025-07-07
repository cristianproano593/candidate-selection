package com.example.customer_service.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LifeExpectancyCalculatorTest {

    private final LifeExpectancyCalculator calc = new LifeExpectancyCalculator();
    private static final int DEFAULT_EXPECTANCY = 80;

    @Test
    void calculateExpectedDate_givenDob_returnsDobPlusDefaultYears() {
        LocalDate dob = LocalDate.of(1950, 5, 20);
        LocalDate expected = dob.plusYears(DEFAULT_EXPECTANCY);
        LocalDate actual = calc.calculateExpectedDate(dob);
        assertEquals(expected, actual,
            "La fecha estimada debe ser fecha de nacimiento + " + DEFAULT_EXPECTANCY + " años");
    }

    @Test
    void calculateExpectedDateByAge_givenAge_returnsNowPlusRemainingYears() {
        int age = 30;
        LocalDate now = LocalDate.now();
        LocalDate expected = now.plusYears(DEFAULT_EXPECTANCY - age);
        LocalDate actual = calc.calculateExpectedDateByAge(age);
        assertEquals(expected, actual,
            "La fecha estimada debe ser hoy + (" + DEFAULT_EXPECTANCY + " - edad) años");
    }

    @Test
    void calculateExpectedDateByAge_ageEqualsDefault_returnsToday() {
        LocalDate actual = calc.calculateExpectedDateByAge(DEFAULT_EXPECTANCY);
        assertEquals(LocalDate.now(), actual,
            "Si la edad = expectativa, debe devolver la fecha de hoy");
    }
}
