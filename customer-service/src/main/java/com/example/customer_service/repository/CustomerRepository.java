package com.example.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.customer_service.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	boolean existsByFirstNameAndLastName(String firstName, String lastName);
}