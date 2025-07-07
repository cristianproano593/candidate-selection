package com.example.customer_service.controller;

import com.example.customer_service.dto.CreateCustomerRequest;
import com.example.customer_service.dto.CustomerDTO;
import com.example.customer_service.dto.MetricDTO;
import com.example.customer_service.service.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer Service", description = "Endpoints para gestionar clientes")
public class CustomerController {

    private final CustomerService svc;

    public CustomerController(CustomerService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(
            @RequestBody @Valid CreateCustomerRequest req
    ) {
        CustomerDTO created = svc.create(req);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> list() {
        return ResponseEntity.ok(svc.listAll());
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricDTO> metrics() {
        return ResponseEntity.ok(svc.calculateMetrics());
    }
}
