package com.example.customer_service.factory;

import com.example.customer_service.dto.CreateCustomerRequest;
import com.example.customer_service.dto.CustomerDTO;
import com.example.customer_service.model.Customer;
import com.example.customer_service.util.LifeExpectancyCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerFactoryTest {

    @Mock
    private LifeExpectancyCalculator calculator;

    @InjectMocks
    private CustomerFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fromCreateRequest_shouldMapAllFieldsAndParseDate() {
        CreateCustomerRequest req = new CreateCustomerRequest();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setAge(40);
        req.setDateOfBirth("1980-05-20");

        Customer result = factory.fromCreateRequest(req);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(40, result.getAge());
        assertEquals(LocalDate.of(1980, 5, 20), result.getDateOfBirth());
    }

    @Test
    void toDto_shouldMapCustomerAndUseCalculator() {
        Customer customer = new Customer();
        customer.setId(123L);
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setAge(30);
        customer.setDateOfBirth(LocalDate.of(1993, 3, 15));

        LocalDate expectedDate = LocalDate.of(2080, 3, 15);
        when(calculator.calculateExpectedDate(customer.getDateOfBirth()))
            .thenReturn(expectedDate);

        CustomerDTO dto = factory.toDto(customer);

        assertEquals(123L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals(30, dto.getAge());
        assertEquals(LocalDate.of(1993, 3, 15), dto.getDateOfBirth());
        assertEquals(expectedDate, dto.getEstimatedEventDate());

        verify(calculator).calculateExpectedDate(customer.getDateOfBirth());
    }
}
