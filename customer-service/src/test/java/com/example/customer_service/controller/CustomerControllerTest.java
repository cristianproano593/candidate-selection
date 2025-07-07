package com.example.customer_service.controller;


import com.example.customer_service.dto.CreateCustomerRequest;
import com.example.customer_service.dto.CustomerDTO;
import com.example.customer_service.dto.MetricDTO;
import com.example.customer_service.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

@Test
void create_ShouldReturnCreatedCustomer() throws Exception {
    CreateCustomerRequest req = new CreateCustomerRequest();
    req.setFirstName("John");
    req.setLastName("Doe");
    req.setAge(30);
    req.setDateOfBirth("1993-04-15");

    CustomerDTO dto = new CustomerDTO(
        1L,
        "John",
        "Doe",
        30,
        LocalDate.parse("1993-04-15"),
        LocalDate.parse("2063-04-15")
    );

    when(customerService.create(any(CreateCustomerRequest.class)))
        .thenReturn(dto);

    mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)           
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.age").value(30))
        .andExpect(jsonPath("$.dateOfBirth").value("1993-04-15"))
        .andExpect(jsonPath("$.estimatedEventDate").value("2063-04-15"));
}
    @Test
    void list_ShouldReturnCustomerList() throws Exception {
        CustomerDTO c1 = new CustomerDTO(
            1L,
            "Alice",
            "Smith",
            25,
            LocalDate.parse("1998-02-20"),
            LocalDate.parse("2068-02-20")
        );
        CustomerDTO c2 = new CustomerDTO(
            2L,
            "Bob",
            "Jones",
            40,
            LocalDate.parse("1983-07-10"),
            LocalDate.parse("2053-07-10")
        );
        List<CustomerDTO> list = Arrays.asList(c1, c2);
        when(customerService.listAll()).thenReturn(list);

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void metrics_ShouldReturnCalculatedMetrics() throws Exception {
        MetricDTO metrics = new MetricDTO(32.5, 7.5, 2);
        when(customerService.calculateMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/customers/metrics"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.averageAge").value(32.5))
            .andExpect(jsonPath("$.standardDeviation").value(7.5))
            .andExpect(jsonPath("$.totalCustomers").value(2));
    }
}
