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

import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerFactory factory;

    public CustomerService(CustomerRepository repo, CustomerFactory factory) {
        this.repo = repo;
        this.factory = factory;
    }

    public CustomerDTO create(CreateCustomerRequest req) {
    	
    	if (repo.existsByFirstNameAndLastName(req.getFirstName(), req.getLastName())) {
            throw new CustomerAlreadyExistsException("Ya existe un cliente con nombre: " +
                req.getFirstName() + " " + req.getLastName());
        }

        Customer entity = factory.fromCreateRequest(req);

     
        Customer saved = repo.save(entity);
        return factory.toDto(saved);
    }

    public List<CustomerDTO> listAll() {
    	List<Customer> list = repo.findAll();
    	if (list.isEmpty()) {
            throw new CustomerNotFoundException("No hay clientes registrados.");
        }
        return list.stream()
                   .map(factory::toDto)
                   .collect(Collectors.toList());
    }

    public MetricDTO calculateMetrics() {
        List<Customer> all = repo.findAll();
        if (all.isEmpty()) {
            throw new InsufficientCustomerDataException("Se necesitan clientes para calcular métricas.");
        }
        IntSummaryStatistics stats = all.stream()
            .mapToInt(Customer::getAge)
            .summaryStatistics();

        double avg = stats.getAverage();
        double variance = all.stream()
            .mapToDouble(c -> Math.pow(c.getAge() - avg, 2))
            .sum() / stats.getCount();
        double stddev = Math.sqrt(variance);

        return new MetricDTO(avg, stddev, (int)stats.getCount());
    }
}