package com.example.auth_service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.auth_service.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
@ActiveProfiles("test")
@DataJpaTest(
  excludeAutoConfiguration = FlywayAutoConfiguration.class,
  properties = {
    // Le indicamos a Hibernate que auto-genere y destruya el esquema
    "spring.jpa.hibernate.ddl-auto=create-drop"
  }
)
@DisplayName("AuthRepository Tests")
class AuthRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AuthRepository userRepository;

  @Test
  @DisplayName("findByUsername existente → devuelve el usuario")
  void whenFindByUsername_thenReturnUser() {
    // dado
    User u = new User();
    u.setUsername("admin");
    u.setPassword("password");
    u.setEnabled(true);
    entityManager.persistAndFlush(u);

    // cuando
    Optional<User> found = userRepository.findByUsername("admin");

    // entonces
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("admin");
  }

  @Test
  @DisplayName("findByUsername no existente → devuelve vacío")
  void whenFindByUsernameNotExists_thenReturnEmpty() {
    Optional<User> found = userRepository.findByUsername("noexiste");
    assertThat(found).isNotPresent();
  }
}
