package com.example.customer_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCustomerRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Min(0)
    private Integer age;
    @NotBlank
    private String dateOfBirth; 

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}
