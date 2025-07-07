package com.example.customer_service.repository;

import com.example.customer_service.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
		  excludeAutoConfiguration = { FlywayAutoConfiguration.class },
		  properties = "spring.jpa.hibernate.ddl-auto=create-drop"
		)
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CustomerRepository repository;

    @Test
    void existsByFirstNameAndLastName_returnsTrue_whenCustomerExists() {
        // dado un cliente persistido
        Customer c = new Customer();
        c.setFirstName("John");
        c.setLastName("Doe");
        c.setAge(30);
        c.setDateOfBirth(LocalDate.parse("1993-04-15"));
        em.persistAndFlush(c);

        // cuando invoco existsByFirstNameAndLastName
        boolean exists = repository.existsByFirstNameAndLastName("John", "Doe");

        // entonces debe ser true
        assertThat(exists).isTrue();
    }

    @Test
    void existsByFirstNameAndLastName_returnsFalse_whenCustomerDoesNotExist() {
        // sin persistir nada
        boolean exists = repository.existsByFirstNameAndLastName("Jane", "Smith");

        // debe ser false
        assertThat(exists).isFalse();
    }
}
