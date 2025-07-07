package com.example.customer_service.dto;

import java.time.LocalDate;

public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate dateOfBirth;
    private LocalDate estimatedEventDate;

    public CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        Integer age,
        LocalDate dateOfBirth,
        LocalDate estimatedEventDate
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.estimatedEventDate = estimatedEventDate;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public LocalDate getEstimatedEventDate() { return estimatedEventDate; }
    public void setEstimatedEventDate(LocalDate estimatedEventDate) {
        this.estimatedEventDate = estimatedEventDate;
    }
}
