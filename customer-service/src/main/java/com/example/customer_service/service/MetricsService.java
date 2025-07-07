package com.example.customer_service.service;

import com.example.customer_service.dto.MetricDTO;
import com.example.customer_service.model.Customer;
import com.example.customer_service.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.List;

@Service
public class MetricsService {

    private final CustomerRepository repo;

    public MetricsService(CustomerRepository repo) {
        this.repo = repo;
    }

    public MetricDTO calculateMetrics() {
        List<Customer> all = repo.findAll();
        if (all.isEmpty()) {
            return new MetricDTO(0.0, 0.0, 0);
        }
        IntSummaryStatistics stats = all.stream()
            .mapToInt(Customer::getAge)
            .summaryStatistics();
       
        double avg = stats.getAverage();
        double variance = all.stream()
            .mapToDouble(c -> Math.pow(c.getAge() - avg, 2))
            .sum() / stats.getCount();
        double stddev = Math.sqrt(variance);

        return new MetricDTO(
            avg,
            stddev,
            all.size()
        );
    }
}
