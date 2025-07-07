package com.example.customer_service.service;

import com.example.customer_service.dto.MetricDTO;
import com.example.customer_service.model.Customer;
import com.example.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsServiceTest {

    @Mock CustomerRepository repo;
    @InjectMocks MetricsService service;

    @Test
    void calculateMetrics_returnsCorrectMetrics() {
        Customer c1 = new Customer(); c1.setAge(20);
        Customer c2 = new Customer(); c2.setAge(40);
        Customer c3 = new Customer(); c3.setAge(60);

        when(repo.findAll()).thenReturn(Arrays.asList(c1, c2, c3));

        MetricDTO m = service.calculateMetrics();

        // avg = 40; variance = ((20-40)²+(40-40)²+(60-40)²)/3 = (400+0+400)/3 ≈ 266.67; stddev ≈ 16.3299; total = 3
        assertThat(m.getAverageAge()).isEqualTo(40.0);
        assertThat(m.getTotalCustomers()).isEqualTo(3);
        assertThat(m.getStandardDeviation())
            .isCloseTo(Math.sqrt(266.6666667), within(1e-6));
    }

    @Test
    void calculateMetrics_empty_returnsZeroStats() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        MetricDTO m = service.calculateMetrics();

        assertThat(m.getTotalCustomers()).isZero();
        assertThat(m.getAverageAge()).isZero();
        assertThat(m.getStandardDeviation()).isZero();
    }
}
