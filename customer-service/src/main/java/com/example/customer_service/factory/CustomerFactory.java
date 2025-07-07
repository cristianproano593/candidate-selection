package com.example.customer_service.factory;

import com.example.customer_service.model.Customer;
import com.example.customer_service.dto.CreateCustomerRequest;
import com.example.customer_service.dto.CustomerDTO;
import com.example.customer_service.util.LifeExpectancyCalculator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class CustomerFactory {

    private final LifeExpectancyCalculator calculator;

    public CustomerFactory(LifeExpectancyCalculator calculator) {
        this.calculator = calculator;
    }

  
    public Customer fromCreateRequest(CreateCustomerRequest req) {
        Customer c = new Customer();
        c.setFirstName(req.getFirstName());
        c.setLastName(req.getLastName());
        c.setAge(req.getAge());
        LocalDate dob = LocalDate.parse(req.getDateOfBirth());
        c.setDateOfBirth(dob);
        return c;
    }

    public CustomerDTO toDto(Customer c) {
        LocalDate estimated = calculator.calculateExpectedDate(c.getDateOfBirth());
        return new CustomerDTO(
            c.getId(),
            c.getFirstName(),
            c.getLastName(),
            c.getAge(),
            c.getDateOfBirth(),
            estimated
        );
    }
}