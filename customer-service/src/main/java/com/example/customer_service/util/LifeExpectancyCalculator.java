package com.example.customer_service.util;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class LifeExpectancyCalculator {

	private static final int DEFAULT_EXPECTANCY = 80;


	public LocalDate calculateExpectedDate(LocalDate dateOfBirth) {
		return dateOfBirth.plusYears(DEFAULT_EXPECTANCY);
	}
	
	public LocalDate calculateExpectedDateByAge(int age) {
		return LocalDate.now().plusYears(DEFAULT_EXPECTANCY - age);
	}
}
