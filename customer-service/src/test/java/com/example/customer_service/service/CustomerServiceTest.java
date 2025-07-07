package com.example.customer_service.service;

import com.example.customer_service.dto.CreateCustomerRequest;
import com.example.customer_service.dto.CustomerDTO;
import com.example.customer_service.dto.MetricDTO;
import com.example.customer_service.exception.CustomerAlreadyExistsException;
import com.example.customer_service.exception.CustomerNotFoundException;
import com.example.customer_service.exception.InsufficientCustomerDataException;
import com.example.customer_service.factory.CustomerFactory;
import com.example.customer_service.model.Customer;
import com.example.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock CustomerRepository repo;
    @Mock CustomerFactory factory;
    @InjectMocks CustomerService service;

    private CreateCustomerRequest req;
    private Customer entity;
    private Customer saved;
    private CustomerDTO dto;

    @BeforeEach
    void setUp() {
        req = new CreateCustomerRequest();
        req.setFirstName("Ana");
        req.setLastName("Pérez");
        req.setAge(30);
        // en lugar de pasarlo como LocalDate, pásalo como String:
        req.setDateOfBirth("1995-01-01");

        entity = new Customer();
        entity.setFirstName("Ana");
        entity.setLastName("Pérez");
        entity.setAge(30);
        entity.setDateOfBirth(LocalDate.of(1995, 1, 1));

        saved = new Customer();
        saved.setId(1L);
        saved.setFirstName("Ana");
        saved.setLastName("Pérez");
        saved.setAge(30);
        saved.setDateOfBirth(LocalDate.of(1995, 1, 1));

        dto = new CustomerDTO(
            1L,
            "Ana",
            "Pérez",
            30,
            LocalDate.of(1995, 1, 1),
            LocalDate.of(2025, 1, 1)
        );
    }


    @Test
    void create_whenNew_savesAndReturnsDto() {
        when(repo.existsByFirstNameAndLastName("Ana","Pérez")).thenReturn(false);
        when(factory.fromCreateRequest(req)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(saved);
        when(factory.toDto(saved)).thenReturn(dto);

        CustomerDTO result = service.create(req);

        assertThat(result).isSameAs(dto);
        verify(repo).save(entity);
    }

    @Test
    void create_whenExists_throws() {
        when(repo.existsByFirstNameAndLastName(anyString(),anyString()))
            .thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
            .isInstanceOf(CustomerAlreadyExistsException.class)
            .hasMessageContaining("Ya existe un cliente");
        verify(repo, never()).save(any());
    }

    @Test
    void listAll_withData_returnsDtos() {
        Customer c1 = new Customer(), c2 = new Customer();
        when(repo.findAll()).thenReturn(Arrays.asList(c1, c2));
        when(factory.toDto(c1)).thenReturn(dto);
        when(factory.toDto(c2)).thenReturn(dto);

        List<CustomerDTO> list = service.listAll();
        assertThat(list).hasSize(2);
    }

    @Test
    void listAll_empty_throws() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(service::listAll)
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("No hay clientes registrados.");
    }

    @Test
    void calculateMetrics_withData_returnsCorrect() {
        Customer c1 = new Customer(); c1.setAge(10);
        Customer c2 = new Customer(); c2.setAge(30);
        when(repo.findAll()).thenReturn(Arrays.asList(c1, c2));

        MetricDTO m = service.calculateMetrics();

        // avg = 20; variance = ((10-20)² + (30-20)²)/2 = (100+100)/2 = 100; stddev = 10; total = 2
        assertThat(m.getAverageAge()).isEqualTo(20.0);
        assertThat(m.getStandardDeviation()).isEqualTo(10.0);
        assertThat(m.getTotalCustomers()).isEqualTo(2);
    }

    @Test
    void calculateMetrics_empty_throws() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(service::calculateMetrics)
            .isInstanceOf(InsufficientCustomerDataException.class)
            .hasMessageContaining("Se necesitan clientes");
    }
}
